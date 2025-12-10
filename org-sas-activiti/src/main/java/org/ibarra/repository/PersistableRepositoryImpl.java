package org.ibarra.repository;


import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import org.hibernate.Hibernate;
import org.hibernate.query.sqm.tree.domain.SqmBasicValuedSimplePath;
import org.hibernate.query.sqm.tree.domain.SqmEntityValuedSimplePath;
import org.hibernate.query.sqm.tree.expression.SqmLiteral;
import org.ibarra.conf.CacheApplication;
import org.ibarra.util.Utils;
import org.ibarra.util.model.BusquedaDinamica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generador de consulta con parametros.
 *
 * @param <T> Tipo de Objeto a castear.
 */
@Repository
public class PersistableRepositoryImpl<T> implements PersistableRepository {
    @PersistenceContext
    private EntityManager em;
    private Class<?> domainClass;
    @Autowired
    private CacheApplication cache;
    private final String entityModel = "entitys_";
    private Map<Long, Map<String, Object>> joins;
    private final static Logger LOG = Logger.getLogger(PersistableRepositoryImpl.class.getName());

    /**
     * Instantiates a new Persistable repository.
     * <p>
     * //     * @param cache the cache
     */
    public PersistableRepositoryImpl() {

    }

    @PostConstruct
    public void init() {
        this.addCacheEntityModel();
        joins = new LinkedHashMap<>();
    }

    public EntityManager getEm() {
        return em;
    }

    /**
     * Crea una cache para los objetos mapeados por el EntityManager.
     */
    private void addCacheEntityModel() {
        try {
            if (this.getEm() != null) {
                LOG.log(Level.INFO, "||||Agregado entidad a cahce " + this.getEm().getEntityManagerFactory().getMetamodel().getEntities().size());
                this.getEm().getEntityManagerFactory().getMetamodel().getEntities().forEach(entityType -> {
                    String name = entityModel + entityType.getName();
                    if (!cache.containsKey(name)) {
                        LOG.log(Level.INFO, "||||Agregado entidad a cahce " + name);
                        cache.add(entityModel + entityType.getName(), entityType);
                    }
                });
            } else {
                LOG.log(Level.INFO, "///EntitiManager");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    /**
     * Implementación para la busqueda dinamica
     *
     * @param busq Modelo con los datos para la busqueda
     * @param <T>  Cualquier tipo de dato
     * @return Listado con los registro encontrado, si el un multiselect devuelve un listado Map<Strng, Object>, caso contrario el listado de la misma entidad.
     */
    @Transactional(readOnly = true)
    @Override
    public <T> List<T> findAllDinamic(BusquedaDinamica busq) {
        try {
            String nameClazz = busq.getEntity();
            Long idTrans = Double.valueOf(ThreadLocalRandom.current().nextDouble() * 100000).longValue();
            boolean isTuple = busq.getFunctions() != null && busq.getFunctions().size() > 1;
            EntityType o = getEntityType(nameClazz);
            if (o == null) {
                System.out.println("No existe objeto en cache model" + nameClazz + " con parametros " + busq);
                return null;
            }
            domainClass = o.getJavaType();
            if (domainClass == null) {
                System.out.println("No existe objeto " + nameClazz + " con parametros " + busq);
                return null;
            }
            CriteriaBuilder builder = this.em.getCriteriaBuilder();
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

    private EntityType getEntityType(String nameClazz) {
        String name = entityModel + nameClazz;
        if (name.contains("entitys_entitys_")) {
            name = name.replace("entitys_entitys_", "entitys_");
        }
        if (!cache.containsKey(name)) {
            this.addCacheEntityModel();
        }
        EntityType o = (EntityType) cache.get(name);
        return o;
    }

    /**
     * Implementación para la busqueda dinamica
     *
     * @param busq    Modelo con los datos para la busqueda
     * @param headers Parametro para agregar en el Header de la respuesta el rootSize con el conteo de los datos encontrados en la consulta.
     * @param <T>     Cualquier tipo de dato
     * @return Listado con los registro encontrado, si el un multiselect devuelve un listado Map<Strng, Object>, caso contrario el listado de la misma entidad.
     */
    @Transactional(readOnly = true)
    @Override
    public <T> List<T> findAllDinamic(BusquedaDinamica busq, MultiValueMap<String, String> headers) {
        try {
            String nameClazz = busq.getEntity();
            Long idTrans = Double.valueOf(ThreadLocalRandom.current().nextDouble() * 100000).longValue();
            boolean isTuple = busq.getFunctions() != null && busq.getFunctions().size() > 1;
            Long count = 0L;
            EntityType o = getEntityType(entityModel + nameClazz);
            domainClass = o.getJavaType();
            if (domainClass == null) {
                System.out.println("No existe objeto " + nameClazz + " con parametros " + busq);
                return null;
            }
            CriteriaBuilder builder = this.em.getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(domainClass);
            if (isTuple) {
                query = builder.createTupleQuery();
            }
            Root from = query.from(domainClass);
            query.select(from);
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
                countQuery.select(builder.countDistinct(from)).where(preds);
            } else {
                countQuery.select(builder.count(from)).where(preds);
            }
            count = em.createQuery(countQuery).getSingleResult();
            if (headers != null) {
                headers.add("rootSize", (count == null ? "0" : count.toString()));
            }
            this.joins.remove(idTrans);
            return resultList;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            headers.add("Error", e.getMessage());
            return null;
        }
    }


    private TypedQuery CriteriaDistinct(BusquedaDinamica filtros, CriteriaQuery query) {
        try {
            if (filtros.getDistinct() != null && Boolean.TRUE.equals(filtros.getDistinct())) {
                query.distinct(filtros.getDistinct());
            }
            TypedQuery typedQuery = this.em.createQuery(query);
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
        List tuples = CriteriaDistinct(busq, query).getResultList();
        if (isTuple) {
            List<Map<String, Object>> resultp = new ArrayList<>();
            for (Tuple single : (List<Tuple>) tuples) {
                Map<String, Object> tempMap = new HashMap<>();
                single.getElements().forEach((v) -> {
                    if (v instanceof SqmBasicValuedSimplePath<?>) {
                        SqmBasicValuedSimplePath dx = (SqmBasicValuedSimplePath) v;
                        tempMap.put(dx.getReferencedPathSource().getPathName(), single.get(v));
                    } else if (v instanceof SqmEntityValuedSimplePath<?>) {
                        SqmEntityValuedSimplePath dx = (SqmEntityValuedSimplePath) v;
                        tempMap.put(dx.getReferencedPathSource().getPathName(), single.get(v));
                    } else {
                        SqmLiteral dx = (SqmLiteral) v;
                        tempMap.put(dx.getLiteralValue().toString(), null);
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
            return tuples;
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
                        join = from.join(sp);
                    } else if (index < (split.length - 1)) {
                        join = join.join(sp);
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
                    From join = from;
                    try {
                        join = createJoin(idTrans, key, from);
                        if ("ASC".equalsIgnoreCase(entry.getValue())) {
                            ordersby.add(builder.asc(join.get(nameField)));
                        } else {
                            ordersby.add(builder.desc(join.get(nameField)));
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
                    if (value instanceof Collection<?> collection) {
                        vals.addAll(collection);
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
                            multSele.add(builder.function(key, clazz, c));
                        }
                    }
                });
                if (multSele.size() > 1) {
                    query.multiselect(multSele);
                } else {
                    query.select(multSele.get(0));
                }
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
//                        System.out.println("NameField " + nameField + " " + condicion + " Join.getJavaType() " + join.getJavaType() + " Atribute Class " + from.getJavaType());
                        findProperty = this.getPredicateField(idTrans, builder, nameField, join, condicion, from, null);
                    } catch (Exception e) {
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
            EntityType o = getEntityType(entityModel + join.getJavaType().getSimpleName());
            Attribute metaField = null;
            Expression path = null;
            if (!nameField.toUpperCase().equalsIgnoreCase("SELECTCASE")) {
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
                System.out.println("----->entityModel " + (entityModel + join.getJavaType().getSimpleName()) + " nameField " + nameField + " condicion " + condicion);
                return null;
            }
            switch (condicion.getComparador().toUpperCase()) {
                case "NOTEQUAL":
                case "NE":
                    return builder.notEqual(path, condicion.getValuesCast(metaField.getJavaType()));
                case "IN":
                    return join.get(nameField).in(condicion.getValuesCast(metaField.getJavaType()));
                case "NOTLIKE":
                case "NOTCONTAINS":
                    return getPredicatelLike(idTrans, builder, nameField, join, condicion, from, path, 1, true, condicionPrin);
                case "LIKE":
                case "CONTAINS":
                    return builder.like(path, condicion.getValueLikes());
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
                    if (condicionPrin.getComparador().equalsIgnoreCase("NOTLIKE") || condicionPrin.getComparador().equalsIgnoreCase("NOTCONTAINS")) {
                        negado1 = true;
                    }
                    return getPredicatelLike(idTrans, builder, nameField, join, condicion, from, path, 2, negado1, condicionPrin);
                case "LT":
                case "<":
                    Object value = condicion.getValuesCast(metaField.getJavaType()).get(0);
                    if (value instanceof Comparable<?> comparable) {
                        return builder.lessThan(path, (Comparable) value);
                    } else {
                        return builder.equal(path, value);
                    }
                case "LTE":
                case "<=":
                    Object value1 = condicion.getValuesCast(metaField.getJavaType()).get(0);
                    if (value1 instanceof Comparable<?> comparable) {
                        return builder.lessThanOrEqualTo(path, (Comparable) value1);
                    } else {
                        return builder.equal(path, value1);
                    }
                case "GT":
                case ">":
                    Object value2 = condicion.getValuesCast(metaField.getJavaType()).get(0);
                    if (value2 instanceof Comparable<?> comparable) {
                        return builder.greaterThan(path, (Comparable) value2);
                    } else {
                        return builder.equal(path, value2);
                    }
                case "GTE":
                case ">=":
                    Object value3 = condicion.getValuesCast(metaField.getJavaType()).get(0);
                    if (value3 instanceof Comparable<?> comparable) {
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
                        if (cd instanceof Map<?, ?> cdv) {
                            nameField = (String) cdv.keySet().stream().findFirst().get();
                            v = (BusquedaDinamica.WhereCondition) Utils.toObjectFromJson(Utils.toJson(cdv.values().stream().findFirst().get()), BusquedaDinamica.WhereCondition.class);
                        } else if (cd instanceof BusquedaDinamica.WhereCondition condition) {
                            v = condition;
                        }
                        System.out.println("Verificando valores " + cd + " where cond " + v);
                        Predicate field = null;
                        if (v.getComparador() == null && cd.toString().startsWith("{") && cd.toString().endsWith("{")) {
                            analizarOrPred(idTrans, builder, from, conds, cd, condicion);
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
                            if (v2 instanceof Map<?, ?> cdv) {
                                nameField = (String) cdv.keySet().stream().findFirst().get();
                                v2 = Utils.toObjectFromJson(Utils.toJson(cdv.values().stream().findFirst().get()), BusquedaDinamica.WhereCondition.class);
                            }
                        } catch (Exception e) {
                            System.out.println("No es js " + e.getMessage());
                            v2 = new BusquedaDinamica.WhereCondition(null, null);
                        }
                        if (v2 instanceof BusquedaDinamica.WhereCondition condition) {
                            vv = this.getPredicateField(idTrans, builder, nameField, join, condition, from, condicion);
                        } else {
                            vv = builder.literal(v2);
                        }
                    }
                    Predicate[] result = new Predicate[conds.size()];
                    System.out.println("Generando case " + conds.size() + " then value " + v2 + " Else " + condElse.size());
                    CriteriaBuilder.Case<Object> when = builder.selectCase().when(builder.and(conds.toArray(result)), vv);
                    if (condElse != null && condElse.size() > 0) {
                        when.otherwise(condElse);
                    }
                    return builder.isTrue(when.as(Boolean.class));
                case "OR":
                    List<Predicate> ors = new ArrayList<>(condicion.getValues().size());
                    for (Object cd : condicion.getValues()) {
                        BusquedaDinamica.WhereCondition v = null;
                        try {
                            v = (BusquedaDinamica.WhereCondition) Utils.toObjectFromJson(cd.toString(), BusquedaDinamica.WhereCondition.class);
                        } catch (Exception e) {
                            System.out.println("No es js " + e.getMessage());
                            v = new BusquedaDinamica.WhereCondition(null, null);
                        }
                        if (v.getComparador() == null && cd.toString().startsWith("{")) {
                            analizarOrPred(idTrans, builder, from, ors, cd, condicion);
                        } else if (v.getComparador() != null) {
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
                            v = (BusquedaDinamica.WhereCondition) Utils.toObjectFromJson(cd.toString(), BusquedaDinamica.WhereCondition.class);
                        } catch (Exception e) {
                            System.out.println("No es js " + e.getMessage());
                            v = new BusquedaDinamica.WhereCondition(null, null);
                        }
                        if (v.getComparador() == null && cd.toString().startsWith("{")) {
                            analizarOrPred(idTrans, builder, from, ands, cd, condicion);
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

                    return builder.equal(path, v);
            }
        } catch (Exception e) {
            System.out.println("----->entityModel " + (entityModel + join.getJavaType().getSimpleName()) + " nameField " + nameField + " condicion " + condicion);
            LOG.log(Level.SEVERE, "", e);
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
                            pred.add(builder.like(path, (ob.toString().endsWith("%") ? ob.toString() : ob.toString().concat("%"))));
                        } else if (i == 2) {
                            pred.add(builder.like(path, (ob.toString().startsWith("%") ? ob.toString() : "%".concat(ob.toString()))));
                        } else {
                            pred.add(builder.like(path, (ob.toString().contains("%") ? ob.toString() : "%".concat(ob.toString()).concat("%"))));
                        }
                    } else {
                        if (i == 1) {
                            pred.add(builder.notLike(path, (ob.toString().endsWith("%") ? ob.toString() : ob.toString().concat("%"))));
                        } else if (i == 2) {
                            pred.add(builder.notLike(path, (ob.toString().startsWith("%") ? ob.toString() : "%".concat(ob.toString()))));
                        } else {
                            pred.add(builder.notLike(path, (ob.toString().contains("%") ? ob.toString() : "%".concat(ob.toString()).concat("%"))));
                        }
                    }
                }
            }
            System.out.println("negado " + negado + " pred " + pred.size());
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
                if (!negado) {
                    if (i == 1) {
                        return builder.like(path, (v.endsWith("%") ? v : v.concat("%")));
                    } else if (i == 2) {
                        return builder.like(path, (v.startsWith("%") ? v : "%".concat(v)));
                    } else {
                        return builder.like(path, (v.contains("%") ? v : "%".concat(v).concat("%")));
                    }
                } else {
                    if (i == 1) {
                        return builder.notLike(path, (v.endsWith("%") ? v : v.concat("%")));
                    } else if (i == 2) {
                        return builder.notLike(path, (v.startsWith("%") ? v : "%".concat(v)));
                    } else {
                        return builder.notLike(path, (v.contains("%") ? v : "%".concat(v).concat("%")));
                    }
                }
            }
        }
    }

    private void analizarOrPred(Long idTrans, CriteriaBuilder builder, Root from, List<Predicate> ors, Object cd, BusquedaDinamica.WhereCondition condicion) {
        if (cd != null) {
            if (cd instanceof String && cd.toString().startsWith("{")) {
                Object o = null;
                try {
                    BusquedaDinamica.WhereCondition x = (BusquedaDinamica.WhereCondition) Utils.toObjectFromJson(cd.toString(), BusquedaDinamica.WhereCondition.class);
                    System.out.println("extracted " + (x.getComparador() == null));
                    if (x.getComparador() == null) {
                        System.out.println("Convirtirndo a mapa ");
                        HashMap<String, Object> fil = (HashMap<String, Object>) Utils.toObjectFromJson(cd.toString(), HashMap.class);
                        o = fil;
                    } else {
                        o = x;
                    }
                } catch (Exception e) {
                    System.out.println("error al convertir " + e);
                }
                analizarOrPred(idTrans, builder, from, ors, o, condicion);
            } else {
                HashMap<String, Object> fil = null;
                if (cd instanceof HashMap) {
                    fil = (HashMap<String, Object>) cd;
                } else {
                    fil = (HashMap<String, Object>) Utils.toObjectFromJson(cd.toString(), HashMap.class);
                }
                BusquedaDinamica d = new BusquedaDinamica();
                d.setFilters(fil);
                List<Predicate> orss = new ArrayList<>(fil.size());
                fil.entrySet().forEach((Map.Entry<String, Object> entry) -> {
                    try {
                        String field = Utils.getUltimaPosicion(entry.getKey(), "\\.");
                        BusquedaDinamica.WhereCondition b = (BusquedaDinamica.WhereCondition) entry.getValue();
                        for (Object o : b.getValues()) {
                            if (o.toString().startsWith("{")) {
                                HashMap<String, Object> cp = new HashMap<>();
                                cp.put(entry.getKey(), o);
                                analizarOrPred(idTrans, builder, from, orss, cp, condicion);
                            } else {
                                orss.add(this.getPredicateField(idTrans, builder, field, this.createJoin(idTrans, entry.getKey(), from), (BusquedaDinamica.WhereCondition) entry.getValue(), from, condicion));
                            }
                        }
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "Value " + entry.getValue(), e);
                    }
                });
                ors.add(builder.or(orss.toArray(new Predicate[]{})));
                return;
            }
        }
    }

    @Transactional
    public Object save(Object entity) {
        try {
            this.getEm().persist(entity);
            this.getEm().refresh(entity);
            return entity;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "--> " + entity, e);
            return null;
        }
    }
}
