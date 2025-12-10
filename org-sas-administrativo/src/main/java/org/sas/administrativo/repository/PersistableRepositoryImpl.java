package org.sas.administrativo.repository;


import com.sun.istack.NotNull;
import org.mapstruct.Mapper;
import org.sas.administrativo.conf.CacheApplication;
import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.util.Constantes;
import org.sas.administrativo.util.EjecucionFuncion;
import org.sas.administrativo.util.GuardarModel;
import org.sas.administrativo.util.Utils;
import org.sas.administrativo.util.model.BusquedaDinamica;
import org.sas.administrativo.util.model.ModelFunction;
import org.hibernate.Hibernate;
import org.hibernate.query.criteria.internal.expression.function.ParameterizedFunctionExpression;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generador de consulta con parametros.
 *
 * @param <T> Tipo de Objeto a castear.
 */
@Repository
@Transactional(readOnly = true)
public class PersistableRepositoryImpl<T> implements PersistableRepository {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private CacheApplication cache;
    protected String entityModel = "entitys_";
    protected String keyClazzDonm = "clazz_trans_";
    private Map<Long, Map<String, Object>> joins;
    private final static Logger LOG = Logger.getLogger(PersistableRepositoryImpl.class.getName());


    /**
     * Instantiates a new Persistable repository.
     */
    @PostConstruct
    public void init() {
        this.addCacheEntityModel();
        joins = new LinkedHashMap<>();
    }

    protected EntityManager getEm() {
        return em;
    }

    @NotNull
    protected Long getIdTrans() {
        Long idTrans = Double.valueOf(Math.random() * 100000).longValue();
        return idTrans;
    }

    /**
     * Crea una cache para los objetos mapeados por el EntityManager.
     */
    private void addCacheEntityModel() {
        try {
            if (this.getEm() != null) {
                this.getEm().getMetamodel().getEntities().forEach(entityType -> {
                    if (!cache.containsKey(entityModel + entityType.getName())) {
                        cache.add(entityModel + entityType.getName(), entityType);
                    }
                });
            } else {
                LOG.log(Level.INFO, "EntitiManager");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    public Class getClazz(String nameClazz) {
        try {
            if (!cache.containsKey(entityModel + nameClazz)) {
                this.addCacheEntityModel();
            }
            EntityType o = (EntityType) cache.get(entityModel + nameClazz);
            if (o == null) {
                LOG.log(Level.INFO, "(getClazz) No existe objeto en cache model " + nameClazz + " con parametros " + nameClazz);
                return null;
            }
            Class domainClass = o.getJavaType();
            return domainClass;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }

    /**
     * Implementación para la busqueda dinamica
     *
     * @param nameClazz Nombre de entidad a buscar
     * @param busq      Modelo con los datos para la busqueda
     * @param <T>       Cualquier tipo de dato
     * @return Listado con los registro encontrado, si el un multiselect devuelve un listado Map<Strng, Object>, caso contrario el listado de la misma entidad.
     */
    @Transactional(readOnly = true)
    public <T> List<T> findAllDinamic(String nameClazz, BusquedaDinamica busq) {
        try {
            Long idTrans = getIdTrans();
            boolean isTuple = busq.getFunctions() != null && busq.getFunctions().size() > 1;
            if (!cache.containsKey(entityModel + nameClazz)) {
                this.addCacheEntityModel();
            }
            EntityType o = (EntityType) cache.get(entityModel + nameClazz);
            if (o == null) {
                LOG.log(Level.INFO, "(findAllDinamic) No existe objeto en cache model " + nameClazz + " con parametros " + busq);
                return null;
            }
            Class domainClass = o.getJavaType();
            if (domainClass == null) {
                LOG.log(Level.INFO, "No existe objeto " + nameClazz + " con parametros " + busq);
                return null;
            }
            Map<String, Object> mapClazzDonm = joins.get(idTrans);
            if (mapClazzDonm == null) {
                mapClazzDonm = new LinkedHashMap<>();
            }
            mapClazzDonm.put(keyClazzDonm + idTrans, domainClass);
            joins.put(idTrans, mapClazzDonm);
            CriteriaBuilder builder = this.getEm().getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(domainClass);
            if (isTuple) {
                query = builder.createTupleQuery();
            }
            Root from = query.from(domainClass);
            processFunction(idTrans, busq, builder, query, from);
            processWhere(idTrans, builder, from, busq.getFilters(), query);
            processGroupBy(idTrans, busq, query, from);
            processOrderBY(idTrans, builder, from, busq.getOrders(), query);
            joins.remove(idTrans);
            return getList(busq, isTuple, query);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    /**
     * Implementación para la busqueda dinamica
     *
     * @param nameClazz Nombre de entidad a buscar
     * @param busq      Modelo con los datos para la busqueda
     * @param headers   Parametro para agregar en el Header de la respuesta el rootSize con el conteo de los datos encontrados en la consulta.
     * @param <T>       Cualquier tipo de dato
     * @return Listado con los registro encontrado, si el un multiselect devuelve un listado Map<Strng, Object>, caso contrario el listado de la misma entidad.
     */
    @Transactional(readOnly = true)
    public <T> List<T> findAllDinamic(String nameClazz, BusquedaDinamica busq, MultiValueMap<String, String> headers) {
        try {
            Long idTrans = getIdTrans();
            boolean isTuple = busq.getFunctions() != null && busq.getFunctions().size() > 0;
            if (!cache.containsKey(entityModel + nameClazz)) {
                this.addCacheEntityModel();
            }
            Long count = 0L;
            EntityType o = (EntityType) cache.get(entityModel + nameClazz);
            if (o == null) {
                LOG.log(Level.INFO, "No existe objeto " + nameClazz + " con parametros " + busq);
                return null;
            }

            Class domainClass = o.getJavaType();
            Map<String, Object> mapClazzDonm = joins.get(idTrans);
            if (mapClazzDonm == null) {
                mapClazzDonm = new LinkedHashMap<>();
            }
            mapClazzDonm.put(keyClazzDonm + idTrans, domainClass);
            joins.put(idTrans, mapClazzDonm);
            if (domainClass == null) {
                LOG.log(Level.INFO, "No existe objeto " + nameClazz + " con parametros " + busq);
                return null;
            }
            CriteriaBuilder builder = this.getEm().getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(domainClass);
            Root from = query.from(domainClass);
            if (isTuple) {
                query = builder.createTupleQuery();
                from = query.from(domainClass);
            } else {
                query.select(from);
            }
            processFunction(idTrans, busq, builder, query, from);
            Predicate[] preds = processWhere(idTrans, builder, from, busq.getFilters(), query);
            processGroupBy(idTrans, busq, query, from);
            processOrderBY(idTrans, builder, from, busq.getOrders(), query);
            joins.remove(idTrans);
            List resultList = getList(busq, isTuple, query);
            // Para realizar el conteo
            CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
            from = countQuery.from(domainClass);
            this.joins.remove(idTrans);
            preds = processWhere(idTrans, builder, from, busq.getFilters(), countQuery);
            if (busq.getDistinct() != null && busq.getDistinct().equals(true)) {
                countQuery.select(builder.countDistinct(from));
            } else {
                countQuery.select(builder.count(from));
            }
            if (preds != null) {
                countQuery.where(preds);
            }
            count = this.getEm().createQuery(countQuery).getSingleResult();
            if (headers != null) {
                headers.add("rootSize", (count == null ? "0" : count.toString()));


                int pageSize = (busq.getPageSize() != null ? busq.getPageSize() : resultList.size());
                long totalItems = (count == null ? 0 : count);
                long totalPages = pageSize > 0 ? (long) Math.ceil((double) totalItems / pageSize) : 1;
                long page;
                if (busq.getFirst() > 0) {
                    page = (long) Math.ceil((((double) (busq.getFirst()) / pageSize))) + 1;
                } else {
                    page = 1;
                }


                headers.add("page", String.valueOf(page));
                headers.add("pageSize", String.valueOf(pageSize));
                headers.add("totalItems", String.valueOf(totalItems));
                headers.add("totalPages", String.valueOf(totalPages));

            }
            this.joins.remove(idTrans);


            return resultList;
        } catch (Exception e) {
            LOG.log(Level.INFO, "Transaccion parametros " + busq);
            LOG.log(Level.SEVERE, "", e);
            headers.add("Error", e.getMessage());
            return null;
        }
    }

    /**
     * Resaliza la ejecución de una funcion de base de datos.
     *
     * @param busq
     * @param headers
     * @return
     */
    @Override
    public Object findAllFunction(BusquedaDinamica busq, MultiValueMap<String, String> headers) {
        return null;
    }


    private TypedQuery CriteriaDistinct(BusquedaDinamica filtros, CriteriaQuery query) {
        try {
            if (filtros.getDistinct() != null && Boolean.TRUE.equals(filtros.getDistinct())) {
                query.distinct(filtros.getDistinct());
            }
            TypedQuery typedQuery = this.getEm().createQuery(query);
            if (filtros.getFirst() != null) {
                typedQuery.setFirstResult(filtros.getFirst());
            }
            if (filtros.getPageSize() != null) {
                typedQuery.setMaxResults(filtros.getPageSize());
            }
            return typedQuery;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "TypeQuery", e);
        }
        return null;
    }

    private <T> List getList(BusquedaDinamica busq, boolean isTuple, CriteriaQuery query) {
        TypedQuery tq = CriteriaDistinct(busq, query);
        if (tq == null) {
            return null;
        }
        List tuples = tq.getResultList();
        if (isTuple) {
            List<Map<String, Object>> resultp = new ArrayList<>();
            for (Tuple single : (List<Tuple>) tuples) {
                Map<String, Object> tempMap = new HashMap<>();
                single.getElements().forEach((v) -> {
                    if (v instanceof ParameterizedFunctionExpression) {
                        ParameterizedFunctionExpression d = (ParameterizedFunctionExpression) v;
                        if (d.getFunctionName() != null) {
                            tempMap.put(d.getFunctionName(), single.get(d.getFunctionName()));
                        }
                    } else {
                        SingularAttributePath path = (SingularAttributePath) v;
                        if (path.getAttribute().getName() != null) {
                            tempMap.put(path.getAttribute().getName(), single.get(path.getAttribute().getName()));
                        }
                    }
                });
                resultp.add(tempMap);
            }
            if (resultp.size() == 0) {
                return null;
            }
            if (busq.getUnproxy()) {
                Hibernate.unproxy(resultp);
            }
            return (List<T>) resultp;
        } else {
            if (Boolean.TRUE.equals(busq.getResolverDto())) {
                try {
                    System.out.println("busq.getResolverDto() " + busq.getResolverDto());
                    ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
                    scanner.addIncludeFilter(new AnnotationTypeFilter(Mapper.class));

                    for (BeanDefinition bd : scanner.findCandidateComponents("org.sas.interna.mappers")) {
                        Class aClass = Class.forName(bd.getBeanClassName());
                        String classMapper = busq.getEntity() + "Mapper";
                        if (aClass.getSimpleName().startsWith(classMapper)) {
                            Object instance = aClass.newInstance();
                            Method[] method = aClass.getMethods();
                            for (Method m : method) {
                                Class<?>[] types = m.getParameterTypes();
                                if ("toDto".equals(m.getName()) && types[0].equals(List.class)) {
                                    System.out.println(aClass.getSimpleName() + " Encontrado " + m + " Lista toDto: " + (tuples == null ? "0" : tuples.size()));
                                    List rsdto = (List) m.invoke(instance, tuples);
                                    return rsdto;
                                }

                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                return tuples;
            } else {
                return tuples;
            }
        }
    }


    private From createJoin(Long idTrans, String key, Root from) {
        try {
            Map<String, Object> j = joins.get(idTrans);
            if (j == null) {
                j = new LinkedHashMap<>();
            }
            String joinKey = key.substring(0, (key.lastIndexOf(".") > 0 ? key.lastIndexOf(".") : key.length()));
            if (j.containsKey(joinKey)) {
                return (From) j.get(joinKey);
            } else {
                From join = from;
                String[] split = key.split("\\.");
                int index = 0;
                for (String sp : split) {
                    if (index == 0 && split.length > 1) {
                        Class c = from.get(sp).getJavaType();
                        if (c.getSimpleName().endsWith("PK") || c.isAnnotationPresent(Embeddable.class)) {
                            join = from;
                        } else {
                            join = from.join(sp);
                        }
                    } else if (index < (split.length - 1)) {
                        Class c = join.get(sp).getJavaType();
                        if (c.getSimpleName().endsWith("PK") || c.isAnnotationPresent(Embeddable.class)) {
                            join = from;
                        } else {
                            join = join.join(sp);
                        }
                    }
                    index++;
                }
                j.put(joinKey, join);
                joins.put(idTrans, j);
                return join;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "findProperty key " + key, e);
        }
        return null;
    }

    private void processGroupBy(Long idTrans, BusquedaDinamica busq, CriteriaQuery query, Root from) {
        try {
            if (busq.getGroupsBy() != null && busq.getGroupsBy().size() > 0) {
                List<Expression<Object>> groups = new ArrayList<>(busq.getFunctions().size());
                busq.getGroupsBy().forEach((key) -> {
                    if (!(key.trim() == "")) {
                        String nameField = Utils.getUltimaPosicion(key, "\\.");
                        From join = from;
                        try {
                            join = createJoin(idTrans, key, from);
                            groups.add(join.get(nameField));
                        } catch (Exception e) {
                            LOG.log(Level.SEVERE, "findProperty key " + key, e);
                        }
                    }
                });
                query.groupBy(groups);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    /**
     * Procesa el mapa de parametro con los ordenamientos para realizar la consulta.
     *
     * @param idTrans
     * @param builder Permite crear las predicados para la conculta.
     * @param from    Root donde se va realizar la selección de la tabla.
     * @param orders  Mapa de parametros con los filtro para los predicados.
     * @param query   Objeto con selección de la tabla
     */
    private void processOrderBY(Long idTrans, CriteriaBuilder builder, Root from, Map<String, String> orders, CriteriaQuery query) {
        try {
            if (orders == null) {
                return;
            }
            if (orders.size() == 0) {
                return;
            }
            List<Order> ordersby = new ArrayList<>(orders.size());
            orders.entrySet().forEach((Map.Entry<String, String> entry) -> {
                String key = entry.getKey();
                if (!(key.trim() == "")) {
                    String nameField = Utils.getUltimaPosicion(key, "\\.");
                    String func = null;
                    if (nameField.contains(":")) {
                        String[] split = nameField.split(":");
                        nameField = split[0];
                        func = split[1];
                    }
                    From join = from;
                    try {
                        join = createJoin(idTrans, key, from);
                        if ("ASC".equalsIgnoreCase(entry.getValue())) {
                            if (func == null) {
                                ordersby.add(builder.asc(join.get(nameField)));
                            } else {
                                Expression length = null;
                                if (func.toLowerCase().contains("length") && func.toLowerCase().contains("trim")) {
                                    length = builder.length(builder.trim(join.get(nameField)));
                                } else if (func.toLowerCase().contains("length")) {
                                    length = builder.length(join.get(nameField));
                                } else if (func.toLowerCase().contains("trim")) {
                                    length = builder.trim(join.get(nameField));
                                }
                                if (length == null) {
                                    ordersby.add(builder.asc(join.get(nameField)));
                                } else {
                                    ordersby.add(builder.asc(length));
                                }
                            }
                        } else {
                            if (func == null) {
                                ordersby.add(builder.desc(join.get(nameField)));
                            } else {
                                Expression length = null;
                                if (func.toLowerCase().contains("length") && func.toLowerCase().contains("trim")) {
                                    length = builder.length(builder.trim(join.get(nameField)));
                                } else if (func.toLowerCase().contains("length")) {
                                    length = builder.length(join.get(nameField));
                                } else if (func.toLowerCase().contains("trim")) {
                                    length = builder.trim(join.get(nameField));
                                }

                                if (length == null) {
                                    ordersby.add(builder.desc(join.get(nameField)));
                                } else {
                                    ordersby.add(builder.desc(length));
                                }
                            }
                        }

                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "findProperty key " + key, e);
                    }
                }
            });
            if (ordersby.size() > 0) {
                query.orderBy(ordersby);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    private void processFunction(Long idTrans, BusquedaDinamica busq, CriteriaBuilder builder, CriteriaQuery query, Root from) {
        try {
            if (busq.getFunctions() != null && busq.getFunctions().size() > 0) {
                List<Expression<Object>> multSele = new ArrayList<>(busq.getFunctions().size());
                busq.getFunctions().forEach((key, value) -> {
                    List<Object> vals = new ArrayList<>();
                    if (value instanceof Collection) {
                        vals.addAll((Collection<?>) value);
                    } else {
                        vals.add(value.toString());
                    }
                    String nameField = "";
                    From join = from;
                    if (vals.size() > 0) {
                        int count_ = 0;
                        Expression[] c = new Expression[vals.size()];
                        for (Object ob : vals) {
                            nameField = Utils.getUltimaPosicion(ob + "", "\\.");
                            join = createJoin(idTrans, ob + "", from);
                            Path path = null;
                            try {
                                path = join.get(nameField);
                            } catch (Exception v) {
                                System.out.println(v.getMessage());
                            }
                            if (path != null) {
                                c[count_] = path;
                            } else {
                                c[count_] = builder.literal(nameField);
                            }
                            count_++;
                        }
                        Class clazz = Object.class;
                        if (key.equalsIgnoreCase(value.toString())) {
                            multSele.addAll(Arrays.<Expression<Object>>asList(c));
                        } else {
                            LOG.log(Level.INFO, "processFunction Key " + key + " clazz " + clazz + " c " + c);
                            multSele.add(builder.function(key, clazz, c));
                        }
                    }
                });
//                if (multSele.size() > 1) {
                query.multiselect(multSele);
//                } else {
//                    query.select(multSele.get(0));
//                }
            } else {
                query.select(from);
            }
        } catch (Exception f) {
            LOG.log(Level.SEVERE, "", f);
        }
    }

    /**
     * Procesa el mapa de parametro con los filtras para realizar los predicados de la consulta.
     *
     * @param idTrans
     * @param builder Permite crear las predicados para la conculta.
     * @param from    Root donde se va realizar la selección de la tabla.
     * @param filtros Mapa de parametros con los filtro para los predicados.
     * @param query   Objeto con selección de la tabla
     * @return
     */
    private Predicate[] processWhere(Long idTrans, CriteriaBuilder builder, Root from, Map<String, Object> filtros, CriteriaQuery query) {
        try {
            List<Predicate> predicates = new ArrayList<>();
            if (filtros == null) {
                return new Predicate[0];
            }
            if (filtros.size() == 0) {
                return new Predicate[0];
            }
            filtros.entrySet().forEach((Map.Entry<String, Object> entry) -> {
                String key = entry.getKey();
                if (!(key.trim() == "")) {
                    BusquedaDinamica.WhereCondition condicion = null;
                    if (entry.getValue() instanceof BusquedaDinamica.WhereCondition) {
                        condicion = (BusquedaDinamica.WhereCondition) entry.getValue();
                    } else {
                        condicion = new BusquedaDinamica.WhereCondition(Arrays.asList(entry.getValue()));
                    }
                    Predicate findProperty = null;
                    From join = from;
                    String nameField = Utils.getUltimaPosicion(key, "\\.");
                    try {
                        join = this.createJoin(idTrans, key, from);
                        findProperty = this.getPredicateField(idTrans, builder, nameField, join, condicion, from, null);
                    } catch (Exception e) {
                        System.out.println("Error al buscar NameField " + nameField + " " + condicion + " Join.getJavaType() " + join.getJavaType());
                        LOG.log(Level.SEVERE, "findProperty key " + key, e);
                    }
                    if (findProperty != null) {
                        predicates.add(findProperty);
                    }
                }
            });
            if (predicates.size() > 0) {
                Predicate[] result = new Predicate[predicates.size()];
                result = predicates.toArray(result);
                query.where(predicates.toArray(result));
                return result;
            }
        } catch (Exception e) {
            System.out.println("BusquedaDinamica " + filtros);
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }

    /**
     * Procesa los predicados para la consulta.
     *
     * @param idTrans
     * @param builder   Permite crear las predicados para la conculta.
     * @param nameField Nombre del campo a filtrar.
     * @param join      Join hacia la tabla relacionada.
     * @param condicion Objecto con la condición where para la tabla.
     * @param from
     * @return Predicado sobre el join realizado
     */
    private Predicate getPredicateField(Long idTrans, CriteriaBuilder builder, String nameField, From join, BusquedaDinamica.WhereCondition condicion, Root from, BusquedaDinamica.WhereCondition condicionPrin) {
        try {
//            if (!cache.containsKey(entityModel + join.getJavaType().getSimpleName())) {
//                this.addCacheEntityModel();
//            }
            EntityType o = (EntityType) cache.get(entityModel + join.getJavaType().getSimpleName());
            Attribute metaField = null;
            Expression path = null;
            if (!nameField.equalsIgnoreCase("SELECTCASE")) {
                if (o == null) {
                    System.out.println(join.getJavaType().getSimpleName() + " No se encontro referencia a " + nameField);
                    return null;
                }
                metaField = o.getAttribute(nameField);
                path = join.get(nameField);
                if (metaField.getJavaType().equals(String.class) || metaField.getJavaType().equals(Character.class)) {
                    if (condicion.getTrim()) {
                        path = builder.trim(builder.upper(join.get(nameField)));
                    } else {
                        path = builder.upper(join.get(nameField));
                    }
                }
            }
            if (condicion == null || condicion.getComparador() == null) {
                LOG.log(Level.INFO, "---> EntityModel " + (entityModel + join.getJavaType().getSimpleName()) + " nameField " + nameField + " condicion " + condicion);
                return null;
            }
            switch (condicion.getComparador().toUpperCase()) {
                case "NOTEQUAL":
                case "NE":
                    List<Object> l = condicion.getValuesCast(metaField.getJavaType());
                    if (l.size() > 1) {
                        return builder.notEqual(path, l);
                    } else {
                        return builder.notEqual(path, l.get(0));
                    }
                case "IN":
                    return join.get(nameField).in(condicion.getValuesCast(metaField.getJavaType()));
                case "NOTIN":
                    return join.get(nameField).in(condicion.getValuesCast(metaField.getJavaType())).not();
                case "NOTLIKE":
                case "NOTCONTAINS":
                    return getPredicatelLike(idTrans, builder, nameField, join, condicion, from, path, 1, true, condicionPrin);
                case "LIKE":
                case "CONTAINS":
                    return builder.like(builder.upper(path), condicion.getValueLikes());
                case "STARTSWITH":
                    if (metaField.getJavaType() == String.class) {
                        boolean negado = false;
                        if (condicionPrin != null && condicionPrin.getComparador() != null) {
                            if ("NOTLIKE".equalsIgnoreCase(condicionPrin.getComparador()) || "NOTCONTAINS".equalsIgnoreCase(condicionPrin.getComparador())) {
                                negado = true;
                            }
                        }
                        return getPredicatelLike(idTrans, builder, nameField, join, condicion, from, path, 1, negado, condicionPrin);
                    } else {
                        Object v = condicion.getValuesCast(metaField.getJavaType()).get(0);
                        if (metaField.getJavaType().equals(Date.class)) {
                            Date date = null;
                            if (v instanceof Date) {
                                date = (Date) v;
                            } else {
                                date = new SimpleDateFormat("yyyy-MM-dd").parse(v.toString());
                            }
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            date = df.parse(df.format(date));
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            cal.add(Calendar.DAY_OF_MONTH, 1);
                            cal.add(Calendar.SECOND, -1);
                            return builder.between(path, date, cal.getTime());
                        } else {
                            return builder.equal(path, v);
                        }

                    }
                case "ENDSWITH":
                    boolean negado1 = false;
                    if (condicionPrin != null && condicionPrin.getComparador() != null) {
                        if ("NOTLIKE".equalsIgnoreCase(condicionPrin.getComparador()) || "NOTCONTAINS".equalsIgnoreCase(condicionPrin.getComparador())) {
                            negado1 = true;
                        }
                    }
                    return getPredicatelLike(idTrans, builder, nameField, join, condicion, from, path, 2, negado1, condicionPrin);
                case "LT":
                case "<":
                    Object value = condicion.getValuesCast(metaField.getJavaType()).get(0);
                    if (value instanceof Comparable) {
                        return builder.lessThan(path, (Comparable) value);
                    } else {
                        return builder.equal(path, value);
                    }
                case "LTE":
                case "<=":
                    Object value1 = condicion.getValuesCast(metaField.getJavaType()).get(0);
                    if (value1 instanceof Comparable) {
                        return builder.lessThanOrEqualTo(path, (Comparable) value1);
                    } else {
                        return builder.equal(path, value1);
                    }
                case "GT":
                case ">":
                    Object value2 = condicion.getValuesCast(metaField.getJavaType()).get(0);
                    if (value2 instanceof Comparable) {
                        return builder.greaterThan(path, (Comparable) value2);
                    } else {
                        return builder.equal(path, value2);
                    }
                case "GTE":
                case ">=":
                    Object value3 = condicion.getValuesCast(metaField.getJavaType()).get(0);
                    if (value3 instanceof String) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            sdf.setLenient(false);
                            value3 = sdf.parse((String) value3);

                        } catch (ParseException ignored) {
                            System.out.println("no se pudo parsear  yyyy-MM-dd");
                        }
                    }
                    if (value3 instanceof Comparable) {
                        return builder.greaterThanOrEqualTo(path, (Comparable) value3);
                    } else {
                        return builder.equal(path, value3);
                    }
                case "BETWEEN":
                    List<Object> valuesCast = condicion.getValuesCast(metaField.getJavaType());
                    return builder.between(path, (Comparable) valuesCast.get(0), (Comparable) valuesCast.get(1));
                case "ISNULL":
                    return builder.isNull(path);
                case "ISNOTNULL":
                    return builder.isNotNull(path);
                case "ISEMPTY":
                    return builder.isEmpty(path);
                case "ISNOTEMPTY":
                    return builder.isNotEmpty(path);
                case "CASE":
                    List<Predicate> conds = new ArrayList<>(condicion.getValues().size());
                    List<Predicate> condElse = new ArrayList<>(condicion.getValues().size());
                    List<Object> values = condicion.getValues();
                    Object v2 = values.get(0);
                    values = values.subList(1, values.size());
                    for (Object cd : values) {
                        BusquedaDinamica.WhereCondition v = null;
                        if (cd instanceof Map) {
                            Map cdv = (Map) cd;
                            nameField = (String) cdv.keySet().stream().findFirst().get();
                            v = (BusquedaDinamica.WhereCondition) Utils.toObjectFromJson(Utils.toJson(cdv.values().stream().findFirst().get()), BusquedaDinamica.WhereCondition.class);
                        } else if (cd instanceof BusquedaDinamica.WhereCondition) {
                            v = (BusquedaDinamica.WhereCondition) cd;
                        }
                        LOG.log(Level.INFO, "Verificando valores " + cd + " where cond " + v);
                        Predicate field = null;
                        if (v.getComparador() == null && cd.toString().startsWith("{") && cd.toString().endsWith("{")) {
                            analizarOrPred(idTrans, builder, from, conds, cd, condicion, null);
                        } else if (v.getComparador() != null) {
                            field = this.getPredicateField(idTrans, builder, nameField, join, v, from, condicion);
                        }
                        if (field != null) {
                            if ("ELSE".equalsIgnoreCase(v.getComparador())) {
                                condElse.add(field);
                            } else {
                                conds.add(field);
                            }
                        }
                    }
                    Expression vv = null;
                    if (v2 != null) {
                        try {
                            if (v2 instanceof Map) {
                                Map cdv = (Map) v2;
                                nameField = (String) cdv.keySet().stream().findFirst().get();
                                v2 = Utils.toObjectFromJson(Utils.toJson(cdv.values().stream().findFirst().get()), BusquedaDinamica.WhereCondition.class);
                            }
                        } catch (Exception e) {
                            LOG.log(Level.INFO, "No es js " + e.getMessage());
                            v2 = new BusquedaDinamica.WhereCondition(null, null);
                        }
                        if (v2 instanceof BusquedaDinamica.WhereCondition) {
                            vv = this.getPredicateField(idTrans, builder, nameField, join, (BusquedaDinamica.WhereCondition) v2, from, condicion);
                        } else {
                            vv = builder.literal(v2);
                        }
                    }
                    Predicate[] result = new Predicate[conds.size()];
                    LOG.log(Level.INFO, "Generando case " + conds.size() + " then value " + v2 + " Else " + condElse.size());
                    CriteriaBuilder.Case<Object> when = builder.selectCase().when(builder.and(conds.toArray(result)), vv);
                    if (condElse != null && condElse.size() > 0) {
                        when.otherwise(condElse);
                    }
                    return builder.isTrue(when.as(Boolean.class));
                case "OR":
                    System.out.println("Ingreso al OR values " + condicion.getValues());
                    List<Predicate> ors = new ArrayList<>(condicion.getValues().size());
                    for (Object cd : condicion.getValues()) {
                        BusquedaDinamica.WhereCondition v = null;
                        Map val = null;
                        try {
                            if (cd instanceof BusquedaDinamica.WhereCondition) {
                                v = (BusquedaDinamica.WhereCondition) cd;
                            } else if (cd instanceof Map) {
                                val = (Map) cd;
                            } else {
                                System.out.println("Or values no es BusquedaDinamica.WhereCondition ni  Map " + cd);
                                if (!cd.toString().startsWith("{")) {
//                                    v = new BusquedaDinamica.WhereCondition("AND", Arrays.asList(cd));
                                } else {
                                    v = (BusquedaDinamica.WhereCondition) Utils.toObjectFromJson(cd.toString(), BusquedaDinamica.WhereCondition.class);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("No es js " + e.getMessage());
                            v = new BusquedaDinamica.WhereCondition(null, null);
                        }
                        if ((v == null || v.getComparador() == null) && val != null) {
                            analizarOrPred(idTrans, builder, from, ors, cd, condicion, nameField);
                        } else if (v != null && v.getComparador() != null) {
                            ors.add(this.getPredicateField(idTrans, builder, nameField, join, v, from, condicion));
                        } else {
                            BusquedaDinamica.WhereCondition oc = new BusquedaDinamica.WhereCondition(Arrays.asList(cd));
                            ors.add(this.getPredicateField(idTrans, builder, nameField, join, oc, from, condicion));
                        }
                    }
                    return builder.or(ors.toArray(new Predicate[]{}));
                case "AND":
                    List<Predicate> ands = new ArrayList<>(condicion.getValues().size());
                    for (Object cd : condicion.getValues()) {
                        BusquedaDinamica.WhereCondition v = null;
                        try {
                            if (!cd.toString().startsWith("{")) {
//                                v = new BusquedaDinamica.WhereCondition("AND", Arrays.asList(cd));
                            } else {
                                v = (BusquedaDinamica.WhereCondition) Utils.toObjectFromJson(cd.toString(), BusquedaDinamica.WhereCondition.class);
                            }
                        } catch (Exception e) {
                            System.out.println("No es js " + e.getMessage());
                            v = new BusquedaDinamica.WhereCondition(null, null);
                        }
                        if (v.getComparador() == null && cd.toString().startsWith("{")) {
                            analizarOrPred(idTrans, builder, from, ands, cd, condicion, null);
                        } else if (v.getComparador() != null) {
                            ands.add(this.getPredicateField(idTrans, builder, nameField, join, v, from, condicion));
                        } else {
                            BusquedaDinamica.WhereCondition oc = new BusquedaDinamica.WhereCondition(Arrays.asList(cd));
                            ands.add(this.getPredicateField(idTrans, builder, nameField, join, oc, from, condicion));
                        }
                    }
                    return builder.and(ands.toArray(new Predicate[]{}));
                default:
                    Object v = null;
                    if (metaField.getJavaType().equals(String.class) || metaField.getJavaType().equals(Character.class)) {
                        if (condicion.getValuesCast(metaField.getJavaType()).size() > 0) {
                            v = condicion.getValuesCast(metaField.getJavaType()).get(0);
                        } else {
                            v = "";
                        }
                    } else {
                        v = condicion.getValuesCast(metaField.getJavaType()).get(0);
                    }
                    if (metaField.getJavaType().equals(Date.class)) {
                        Date date = null;
                        if (v instanceof Date) {
                            date = (Date) v;
                        } else {
                            date = new SimpleDateFormat("yyyy-MM-dd").parse(v.toString());
                        }
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        date = df.parse(df.format(date));
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        cal.add(Calendar.SECOND, -1);
                        return builder.between(path, date, cal.getTime());
                    } else {
                        return builder.equal(path, v);
                    }
            }
        } catch (Exception e) {
            System.out.println("----->entityModel " + (entityModel + join.getJavaType().getSimpleName()) + " nameField " + nameField + " condicion " + condicion);
            LOG.log(Level.SEVERE, "entityModel", e);
            return null;
        }
    }

    private Predicate getPredicatelLike(Long idTrans, CriteriaBuilder builder, String nameField, From join, BusquedaDinamica.WhereCondition condicion, Root from, Expression path, int i, boolean negado, BusquedaDinamica.WhereCondition condicionPrin) {
        if (condicion.getValues() != null && condicion.getValues().size() > 1) {
            List<Predicate> pred = new ArrayList<>(condicion.getValues().size());
            for (Object ob : condicion.getValues()) {
                if (ob instanceof Map) {
                    pred.add(this.getPredicateField(idTrans, builder, nameField, join, (BusquedaDinamica.WhereCondition) Utils.toObjectFromJson(Utils.toJson(ob), BusquedaDinamica.WhereCondition.class), from, condicionPrin));
                } else {
                    if (!negado) {
                        if (i == 1) {
                            pred.add(builder.like(builder.upper(path), (ob.toString().endsWith("%") ? ob.toString().toUpperCase() : ob.toString().toUpperCase().concat("%"))));
                        } else if (i == 2) {
                            pred.add(builder.like(builder.upper(path), (ob.toString().startsWith("%") ? ob.toString().toUpperCase() : "%".concat(ob.toString().toUpperCase()))));
                        } else {
                            pred.add(builder.like(builder.upper(path), (ob.toString().contains("%") ? ob.toString().toUpperCase() : "%".concat(ob.toString().toUpperCase()).concat("%"))));
                        }
                    } else {
                        if (i == 1) {
                            pred.add(builder.notLike(builder.upper(path), (ob.toString().endsWith("%") ? ob.toString().toUpperCase() : ob.toString().toUpperCase().concat("%"))));
                        } else if (i == 2) {
                            pred.add(builder.notLike(builder.upper(path), (ob.toString().startsWith("%") ? ob.toString().toUpperCase() : "%".concat(ob.toString().toUpperCase()))));
                        } else {
                            pred.add(builder.notLike(builder.upper(path), (ob.toString().contains("%") ? ob.toString().toUpperCase() : "%".concat(ob.toString().toUpperCase()).concat("%"))));
                        }
                    }
                }
            }
            LOG.log(Level.INFO, "negado " + negado + " pred " + pred.size());
            if (condicionPrin != null && "OR".equalsIgnoreCase(condicionPrin.getComparador())) {
                return builder.or(pred.toArray(new Predicate[pred.size()]));
            } else {
                return builder.and(pred.toArray(new Predicate[pred.size()]));
            }
        } else {
            Object vd = condicion.getValues().get(0);
            if (vd instanceof Map) {
                return getPredicatelLike(idTrans, builder, nameField, join, (BusquedaDinamica.WhereCondition) Utils.toObjectFromJson(Utils.toJson(vd), BusquedaDinamica.WhereCondition.class), from, path, i, negado, condicion);
            } else {
                String v = condicion.getValues().get(0).toString();
                v = v.toUpperCase();
                if (!negado) {
                    if (i == 1) {
                        return builder.like(builder.upper(path), (v.endsWith("%") ? v : v.concat("%")));
                    } else if (i == 2) {
                        return builder.like(builder.upper(path), (v.startsWith("%") ? v : "%".concat(v)));
                    } else {
                        return builder.like(builder.upper(path), (v.contains("%") ? v : "%".concat(v).concat("%")));
                    }
                } else {
                    if (i == 1) {
                        return builder.notLike(builder.upper(path), (v.endsWith("%") ? v : v.concat("%")));
                    } else if (i == 2) {
                        return builder.notLike(builder.upper(path), (v.startsWith("%") ? v : "%".concat(v)));
                    } else {
                        return builder.notLike(builder.upper(path), (v.contains("%") ? v : "%".concat(v).concat("%")));
                    }
                }
            }
        }
    }

    private void analizarOrPred(Long idTrans, CriteriaBuilder builder, Root from, List<Predicate> ors, Object cd, BusquedaDinamica.WhereCondition condicion, String nameField) {
        if (cd != null) {
            if (cd instanceof String && cd.toString().startsWith("{")) {
                try {
                    BusquedaDinamica.WhereCondition x = (BusquedaDinamica.WhereCondition) Utils.toObjectFromJson(cd.toString(), BusquedaDinamica.WhereCondition.class);
                    Object o = (x.getComparador() == null) ? Utils.toObjectFromJson(cd.toString(), HashMap.class) : x;
                    analizarOrPred(idTrans, builder, from, ors, o, condicion, null);
                } catch (Exception e) {
                    System.out.println("error al convertir " + e);
                }
            } else {
                HashMap<String, Object> fil = (cd instanceof HashMap) ? (HashMap<String, Object>) cd : (HashMap<String, Object>) Utils.toObjectFromJson(cd.toString(), HashMap.class);
                BusquedaDinamica d = new BusquedaDinamica();
                d.setFilters(fil);
                List<Predicate> orss = new ArrayList<>(fil.size());
                fil.entrySet().forEach((Map.Entry<String, Object> entry) -> {
                    try {
                        BusquedaDinamica.WhereCondition b = (BusquedaDinamica.WhereCondition) Utils.toObjectFromJson(cd.toString(), BusquedaDinamica.WhereCondition.class);
                        for (Object o : b.getValues()) {
                            if (o.toString().startsWith("{")) {
                                HashMap<String, Object> cp = new HashMap<>();
                                cp.put(entry.getKey(), o);
                                analizarOrPred(idTrans, builder, from, orss, cp, condicion, null);
                            } else {
                                orss.add(this.getPredicateField(idTrans, builder, nameField, this.createJoin(idTrans, entry.getKey(), from), (BusquedaDinamica.WhereCondition) Utils.toObjectFromJson(cd.toString(), BusquedaDinamica.WhereCondition.class), from, condicion));
                            }
                        }
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "Value " + entry.getValue(), e);
                    }
                });
                ors.add(builder.or(orss.toArray(new Predicate[]{})));
            }
        }
    }

    public RespuestaWs save(GuardarModel model) {
        RespuestaWs rws = new RespuestaWs();
        rws.setEstado(false);
        String msg = "";
        boolean array = false;
        try {
            if (model == null) {
                msg += Constantes.NO_DATO_ERROR_MENSAJE + "\n";
            }
            if (model.getEntity() == null) {
                msg += Constantes.NO_DATO_ERROR_MENSAJE + " del Objeto" + "\n";
            }
            if (model.getData() == null) {
                msg += Constantes.NO_DATO_ERROR_MENSAJE + " del cuerpo del Objeto" + "\n";
            }
            if (!cache.containsKey(entityModel + model.getEntity())) {
                this.addCacheEntityModel();
            }
            rws.setMensaje(msg);
            EntityType o = (EntityType) cache.get(entityModel + model.getEntity());
            if (o == null) {
                msg += "No existe objeto " + model.getEntity() + " con parametros " + model.getEntity() + "\n";
                System.out.println(model);
                return rws;
            }
            Class entityClass = o.getJavaType();
            if (entityClass == null) {
                msg += "No existe objeto " + model.getEntity() + " con parametros " + model.getEntity() + "\n";
                System.out.println(model);
                return rws;
            }
            if (model.getData() == null) {
                msg += "No existe información a guardar.\n";
                System.out.println(model);
                return rws;
            }
            if (model.getData().startsWith("[")) {
                array = true;
                Object x = Array.newInstance(entityClass, 1);
                System.out.println("Nueva instancia " + x.getClass());
                entityClass = x.getClass();
            }
            rws.setMensaje(msg);
            List d = new ArrayList();
            if (array) {
                Object[] ao = (Object[]) Utils.toObjectFromJson(model.getData(), entityClass);
                System.out.println(Arrays.toString(ao));
                d.addAll(Arrays.asList(ao));
            } else {
                d.add(Utils.toObjectFromJson(model.getData(), entityClass));
            }
            rws.setEstado(true);
            Object datas = null;
            List rs = new ArrayList();
            for (Object entity : d) {
                boolean ok = this.processValidations(rws, entity, model);
                System.out.println("Guardar " + ok);
                if (ok) {
                    SingularAttribute id = o.getDeclaredId(o.getIdType().getJavaType());
                    Field field = entity.getClass().getDeclaredField(id.getName());
                    field.setAccessible(true);
                    Object idVal = field.get(entity);
                    if (idVal == null) {
                        this.getEm().persist(entity);
                        this.getEm().refresh(entity);
                    } else {
                        this.getEm().merge(entity);
                    }
                    datas = entity;
                    rs.add(entity);
                    rws.setEstado(true);
                } else {
                    rws.setEstado(false);
                    break;
                }
            }
            if (Utils.isNotEmpty(rs)) {
                try {
                    rws.setData(Utils.toJson(rs));
                } catch (Exception e) {
                    System.out.println("Error al convertir save(GuardarModel model) " + e.getMessage());
                }
            }
            if (datas != null) {
                try {
                    Field f = datas.getClass().getDeclaredField("id");
                    f.setAccessible(true);
                    Object idVal = f.get(datas);
                    rws.setId(Long.valueOf(idVal + ""));
                    //rws.setId(f.getLong(f.get(datas)));
                } catch (Exception e) {
                    System.out.println("Error al obtener id " + e.getMessage());
                }
            }
            if (rws.getEstado()) {
                rws.setEstado(true);
                rws.setMensaje(Constantes.DATOS_GUARDADOS);
            } else {
                rws.setEstado(false);
//                rws.setMensaje(Constantes.DATOS_NO_GUARDADOS);
            }
            System.out.println("Respuesta guardado generico " + rws);
            return rws;
        } catch (Exception e) {
            rws.setEstado(false);
            rws.setMensaje(e.getMessage());
            LOG.log(Level.SEVERE, "", e);
            this.getEm().getTransaction().rollback();
        }
        return rws;
    }

    @Transactional
    public Object save(Object entity) {
        try {
            EntityType o = (EntityType) cache.get(entityModel + entity.getClass().getSimpleName());
            SingularAttribute id = o.getDeclaredId(o.getIdType().getJavaType());
            Field field = entity.getClass().getDeclaredField(id.getName());
            field.setAccessible(true);
            Object idVal = field.get(entity);
            if (idVal == null) {
                this.getEm().persist(entity);
                this.getEm().refresh(entity);
            } else {
                update(entity);
            }
            return entity;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "-->", e);
            return null;
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Object save1(Object entity) {
        try {
            this.getEm().joinTransaction();
//            this.getEm().getTransaction().begin();
            this.getEm().clear();
            this.getEm().persist(entity);
//            this.getEm().getTransaction().commit();
//            this.getEm().flush();
//            this.getEm().refresh(entity);
            return entity;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "-->", e);
            return null;
        }
    }

    @Transactional
    public Object update(Object entity) {
        try {
            this.getEm().merge(entity);
            return entity;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "-->", e);
            return null;
        }
    }

    private boolean processValidations(RespuestaWs rws, Object entity, GuardarModel model) {
        String msg = "";
        if (rws.getMensaje() != null) {
            msg = rws.getMensaje();
        }
//        System.out.println("Modelo guardado " + model + " v " + (model.getValidaciones() != null));
        try {
            if (model.getValidaciones() != null) {
                for (Map.Entry<String, GuardarModel.Validacion> e : model.getValidaciones().entrySet()) {
                    try {
                        String[] split = e.getKey().split("\\.");
                        int index = 0;
                        Object v = entity;
                        for (String sp : split) {
                            Field f = v.getClass().getDeclaredField(sp);
                            f.setAccessible(true);
                            Object o = f.get(v);
                            System.out.println("Campo " + sp + " Objeto a buscar " + o + " Validacion " + e.getValue());
                            if (index == 0 && split.length >= 1) { // referencia dentro de la propiedad
                                if (e.getValue() != null && e.getValue().getAcciones() != null) {
                                    for (String vf : e.getValue().getAcciones()) {
                                        if ("ISNOTNULL".equalsIgnoreCase(vf)) {
                                            if (o == null) {
                                                msg += String.format(Constantes.FALTA_INGRESAR_CAMPO, "Ingresar o Seleccionar", e.getValue().getDescripcionCampo()) + "\n";
                                            }
                                            if (o instanceof String) {
                                                if (o.toString().trim().length() == 0) {
                                                    msg += String.format(Constantes.FALTA_INGRESAR_CAMPO, "Ingresar o Seleccionar", e.getValue().getDescripcionCampo()) + "\n";
                                                    System.out.println(model);
                                                }
                                            }
                                        } else if (">".equalsIgnoreCase(vf)) {
                                            if (o != null) {
                                                BigDecimal bd = new BigDecimal(o.toString());
                                                if (bd.compareTo(BigDecimal.ZERO) <= 0) {
                                                    msg += String.format(Constantes.EXISTE_REGISTRO, "Mayor", "0") + "\n";
                                                }
                                            }
                                        } else if ("<".equalsIgnoreCase(vf)) {
                                            if (o != null) {
                                                BigDecimal bd = new BigDecimal(o.toString());
                                                if (bd.compareTo(BigDecimal.ZERO) <= 0) {
                                                    msg += String.format(Constantes.EXISTE_REGISTRO, "Menor", "0") + "\n";
                                                }
                                            }
                                        }

                                    }
                                }
                            } else if (index < (split.length - 1)) { // Si es el primero o el ultimo
                                System.out.println("No se hace nada...");
                            }
                            index++;
                        }
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, "-->", ex);
                    }
                }
            }
            if ("".equalsIgnoreCase(msg)) {
                return true;
            } else {
                rws.setMensaje(msg);
                rws.setEstado(false);
                return false;
            }
        } catch (Exception e) {
            rws.setEstado(false);
            rws.setMensaje(e.getMessage());
            LOG.log(Level.SEVERE, "--> ", e);
            return false;
        }
    }

    //    @Override
    public Object findAllFunction(EjecucionFuncion funcion) {
        try {
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "findAllFunction", e);
        }
        return null;
    }

    /**
     * Guarda lista de objetos sin validación.
     *
     * @param list Lista de objetos guardados.
     */
//    @Override
    @Transactional
    public Collection saveAll(Collection list) {
        try {
            Collection c = new ArrayList(list.size());
            for (Object entity : list) {
                this.getEm().persist(entity);
                this.getEm().refresh(entity);
                c.add(entity);
            }
            return c;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "--> Error el persistir entidad ", e);
            this.getEm().getTransaction().rollback();
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Object find(BusquedaDinamica busq) {
        try {
            String nameClazz = busq.getEntity();
            Long idTrans = getIdTrans();
            boolean isTuple = busq.getFunctions() != null && busq.getFunctions().size() > 1;
            if (!cache.containsKey(entityModel + nameClazz)) {
                this.addCacheEntityModel();
            }
            EntityType o = (EntityType) cache.get(entityModel + nameClazz);
            if (o == null) {
                LOG.log(Level.INFO, "(find) No existe objeto en cache model " + nameClazz + " con parametros " + busq);
                return null;
            }
            Class domainClass = o.getJavaType();
            if (domainClass == null) {
                LOG.log(Level.INFO, "No existe objeto " + nameClazz + " con parametros " + busq);
                return null;
            }
            Map<String, Object> mapClazzDonm = joins.get(idTrans);
            if (mapClazzDonm == null) {
                mapClazzDonm = new LinkedHashMap<>();
            }
            mapClazzDonm.put(keyClazzDonm + idTrans, domainClass);
            joins.put(idTrans, mapClazzDonm);
            CriteriaBuilder builder = this.getEm().getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(domainClass);
            if (isTuple) {
                query = builder.createTupleQuery();
            }
            Root from = query.from(domainClass);
            processFunction(idTrans, busq, builder, query, from);
            processWhere(idTrans, builder, from, busq.getFilters(), query);
            processGroupBy(idTrans, busq, query, from);
            processOrderBY(idTrans, builder, from, busq.getOrders(), query);
            joins.remove(idTrans);
            List list = getList(busq, isTuple, query);
            if (Utils.isNotEmpty(list)) {
                return list.get(0);
            }
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    @Transactional(readOnly = true)
    public boolean exists(BusquedaDinamica busq) {
        try {
            Long idTrans = getIdTrans();
            EntityType o = (EntityType) cache.get(entityModel + busq.getEntity());
            if (o == null) {
                LOG.log(Level.INFO, "(exists) No existe objeto en cache model " + busq.getEntity() + " con parametros " + busq);
                return false;
            }
            Class domainClass = o.getJavaType();
            if (domainClass == null) {
                LOG.log(Level.INFO, "(exists) No existe objeto " + busq.getEntity() + " con parametros " + busq);
                return false;
            }
            CriteriaBuilder builder = this.getEm().getCriteriaBuilder();
            CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

            Map<String, Object> mapClazzDonm = joins.get(idTrans);
            if (mapClazzDonm == null) {
                mapClazzDonm = new LinkedHashMap<>();
            }
            mapClazzDonm.put(keyClazzDonm + idTrans, domainClass);
            joins.put(idTrans, mapClazzDonm);

            Root from = countQuery.from(domainClass);
            Predicate[] preds = processWhere(idTrans, builder, from, busq.getFilters(), countQuery);
            if (busq.getDistinct() != null && busq.getDistinct().equals(true)) {
                if (preds == null) {
                    countQuery.select(builder.countDistinct(from));
                } else {
                    countQuery.select(builder.countDistinct(from)).where(preds);
                }
            } else {
                if (preds == null) {
                    countQuery.select(builder.count(from));
                } else {
                    countQuery.select(builder.count(from)).where(preds);
                }
            }
            Long count = this.getEm().createQuery(countQuery).getSingleResult();
            this.joins.remove(idTrans);
            if (count == null) {
                count = 0L;
            }
            return count > 0;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return false;
        }
    }

    @Transactional
    public boolean deleteModelAll(GuardarModel model) {
        try {
            boolean array = false;
            if (!cache.containsKey(entityModel + model.getEntity())) {
                this.addCacheEntityModel();
            }
            EntityType o = (EntityType) cache.get(entityModel + model.getEntity());
            if (o == null) {
                System.out.println("No existe objeto " + model.getEntity() + " con parametros " + model.getEntity());
                return false;
            }
            Class entityClass = o.getJavaType();
            if (entityClass == null) {
                System.out.println("No existe objeto " + model.getEntity() + " con parametros " + model.getEntity());
                return false;
            }
            if (model.getData() == null) {
                System.out.println("No existe información a guardar.");
                return false;
            }
            if (model.getData().startsWith("[")) {
                array = true;
                Object x = Array.newInstance(entityClass, 1);
                System.out.println("Nueva instancia " + x.getClass());
                entityClass = x.getClass();
            }
            List entitiList = new ArrayList();
            if (array) {
                Object[] ao = (Object[]) Utils.toObjectFromJson(model.getData(), entityClass);
                System.out.println(Arrays.toString(ao));
                entitiList.addAll(Arrays.asList(ao));
            } else {
                entitiList.add(Utils.toObjectFromJson(model.getData(), entityClass));
            }
            boolean deleteAll = deleteAll(entitiList);
            System.out.println("Respuesta guardado generico registro eliminados" + deleteAll);
            return deleteAll;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "--> Error el persistir entidad ", e);
            this.getEm().getTransaction().rollback();
        }
        return false;
    }

    @Transactional
    public boolean deleteAll(Collection list) {
        try {
            for (Object entity : list) {
                if (!cache.containsKey(entityModel + entity.getClass().getSimpleName())) {
                    this.addCacheEntityModel();
                }
                EntityType o = (EntityType) cache.get(entityModel + entity.getClass().getSimpleName());
                if (o == null) {
                    System.out.println("No existe objeto " + entity.getClass().getSimpleName() + " con parametros " + entity.getClass().getSimpleName());
                    return false;
                }
                CriteriaDelete<?> criteriaDelete = this.getEm().getCriteriaBuilder().createCriteriaDelete(entity.getClass());
                Root from = criteriaDelete.from(o.getJavaType());
                Field f = entity.getClass().getDeclaredField("id");
                f.setAccessible(true);
                Object idVal = f.get(entity);
                criteriaDelete.where(this.getEm().getCriteriaBuilder().equal(from.get("id"), idVal));
                int i = this.getEm().createQuery(criteriaDelete).executeUpdate();
            }
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "--> Error el persistir entidad ", e);
            this.getEm().getTransaction().rollback();
        }
        return false;
    }


}
