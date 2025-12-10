package org.sas.administrativo.repository;

import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.util.GuardarModel;
import org.sas.administrativo.util.model.BusquedaDinamica;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * Permite realizar las busquedas usando un modelo de datos {@link BusquedaDinamica}
 */
public interface PersistableRepository {
    /**
     * Implementación para la busqueda dinamica
     *
     * @param clazz   Nombre de entidad a buscar
     * @param filtros Modelo con los datos para la busqueda
     * @param <T>     Cualquier tipo de dato
     * @return Listado con los registro encontrado, si el un multiselect devuelve un listado Map<Strng, Object>, caso contrario el listado de la misma entidad.
     */
    <T> List<T> findAllDinamic(String clazz, BusquedaDinamica filtros);

    /**
     * Implementación para la busqueda dinamica
     *
     * @param nameClazz Nombre de entidad a buscar
     * @param busq      Modelo con los datos para la busqueda
     * @param headers   Parametro para agregar en el Header de la respuesta el rootSize con el conteo de los datos encontrados en la consulta.
     * @param <T>       Cualquier tipo de dato
     * @return Listado con los registro encontrado, si el un multiselect devuelve un listado Map<Strng, Object>, caso contrario el listado de la misma entidad.
     */
    <T> List<T> findAllDinamic(String nameClazz, BusquedaDinamica busq, MultiValueMap<String, String> headers);

    /**
     * Resaliza la ejecución de una funcion de base de datos.
     *
     * @param data
     * @param headers
     * @return
     */
    public Object findAllFunction(BusquedaDinamica data, MultiValueMap<String, String> headers);

    public RespuestaWs save(GuardarModel model);

    public boolean exists(BusquedaDinamica busq);

    public Object update(Object entity);
}