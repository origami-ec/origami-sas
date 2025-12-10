package org.sas.administrativo.repository.configuracion;

import org.sas.administrativo.entity.configuracion.CatalogoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Catalogo mef repository.
 */
@Repository
public interface CatalogoItemRepository extends JpaRepository<CatalogoItem, Long> {
    CatalogoItem findByCodigoAndEstado(String codigo, String estado);

    @Query("select ci from CatalogoItem ci where ci.estado=:estadoItem and ci.catalogo.codigo=:codigo and ci.catalogo.estado=:estado")
    List<CatalogoItem> findByItemsCatalogo(String estadoItem, String codigo, String estado);

    List<CatalogoItem> findAllByCatalogo_IdOrderByOrdenAsc(Long catalogo);

    CatalogoItem findByCatalogo_CodigoAndCodigo(String cod_catalogo, String cod_item);

    @Query("SELECT ci FROM CatalogoItem ci WHERE ci.estado = 'ACTIVO' AND ci.codigo = :codigo GROUP BY ci.texto, ci.id")
    List<CatalogoItem> findByCatalogo_CodigoOrderByOrdenGroup(String codigo);

    @Query("SELECT ci FROM CatalogoItem ci WHERE ci.estado = 'ACTIVO' AND ci.catalogo.codigo = :codigo ORDER BY ci.orden")
    List<CatalogoItem> findByCatalogo_CodigoOrderByOrden(String codigo);

    @Query("select max(ci.orden) from CatalogoItem ci where ci.catalogo.id = :idcatalogo and ci.estado = 'ACTIVO' ")
    Integer maxOrdenCatalogo(Long idcatalogo);

    List<CatalogoItem> findAllByCodigoAndEstado(String codigo, String activo);










}