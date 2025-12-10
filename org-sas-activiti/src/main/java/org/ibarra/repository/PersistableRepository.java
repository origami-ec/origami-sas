package org.ibarra.repository;

import org.ibarra.util.model.BusquedaDinamica;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * Permite realizar las busquedas usando un modelo de datos {@link BusquedaDinamica}
 */
public interface PersistableRepository {
    /**
     * Implementación para la busqueda dinamica
     *
     * @param filtros Modelo con los datos para la busqueda
     * @param <T>     Cualquier tipo de dato
     * @return Listado con los registro encontrado, si el un multiselect devuelve un listado Map<Strng, Object>, caso contrario el listado de la misma entidad.
     */
    <T> List<T> findAllDinamic(BusquedaDinamica filtros);

    /**
     * Implementación para la busqueda dinamica
     *
     * @param busq      Modelo con los datos para la busqueda
     * @param headers   Parametro para agregar en el Header de la respuesta el rootSize con el conteo de los datos encontrados en la consulta.
     * @param <T>       Cualquier tipo de dato
     * @return Listado con los registro encontrado, si el un multiselect devuelve un listado Map<Strng, Object>, caso contrario el listado de la misma entidad.
     */
    <T> List<T> findAllDinamic(BusquedaDinamica busq, MultiValueMap<String, String> headers);

    public Object save(Object entity);
}
