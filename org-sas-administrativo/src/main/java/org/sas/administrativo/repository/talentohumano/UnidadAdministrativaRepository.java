package org.sas.administrativo.repository.talentohumano;

import org.sas.administrativo.entity.talentohumano.ServidorCargo;
import org.sas.administrativo.entity.talentohumano.UnidadAdministrativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * the interface UnidadAdministrativa Repository
 */
@Repository
public interface UnidadAdministrativaRepository extends JpaRepository<UnidadAdministrativa, Long> {

    List<UnidadAdministrativa> findUnidadAdministrativaByPadreIsNullAndEstado(String estado);

    List<UnidadAdministrativa> findAllByPadre_IdAndEstado(Long id, String estado);

    List<UnidadAdministrativa> findAllByEstadoAndTipo_Codigo(String estado, String tipo);


    List<UnidadAdministrativa> findAllByEstadoAndTipo_CodigoIn(String estado, List<String> tipos);

    @Query("SELECT sc FROM ServidorCargo sc " +
            "JOIN sc.cargo ca " +
            "JOIN ca.unidad uni " +
            "WHERE uni.padre.id = :padre")
    List<ServidorCargo> getListServidoresbyDireccion(@Param("padre") Long padre);

    Optional<UnidadAdministrativa> findById(Long id);

    @Query("SELECT u FROM UnidadAdministrativa u WHERE u.padre.id = :idUnidad")
    List<UnidadAdministrativa> findHijosByUnidad(@Param("idUnidad") Long idUnidad);

    @Query("SELECT u FROM UnidadAdministrativa u WHERE u.padre in :idUnidad")
    List<UnidadAdministrativa> listarUnidadesPorUnaListaDePadres(@Param("idUnidad") List<UnidadAdministrativa> unidades);

    @Query(value = "select * from talento_humano.obtener_unidad_encargo(?1)", nativeQuery = true)
    String buscarUnidadAdmistrativaServidor(Long idservidor);
}