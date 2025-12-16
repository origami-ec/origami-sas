package org.ibarra.repository;

import org.ibarra.entity.HistoricoTramiteObservacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoricoTramiteObservacionRepo extends JpaRepository<HistoricoTramiteObservacion, Long> {

    List<HistoricoTramiteObservacion> findAllByTramite_IdAndEstadoTrueOrderByFechaCreacionAsc(Long tramite);

    HistoricoTramiteObservacion findFirstByTramite_idOrderByIdDesc(Long tramite);

    @Query(value = "SELECT h.usuario_creacion FROM procesos.historico_tramite_observacion h WHERE h.tramite = :tramite AND h.tarea = :tarea ORDER BY h.id DESC LIMIT 1", nativeQuery = true)
    String usuarioPorTramite(Long tramite, String tarea);

}
