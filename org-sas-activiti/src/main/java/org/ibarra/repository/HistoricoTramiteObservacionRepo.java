package org.ibarra.repository;

import org.ibarra.entity.HistoricoTramiteObservacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoTramiteObservacionRepo extends JpaRepository<HistoricoTramiteObservacion, Long> {

    List<HistoricoTramiteObservacion> findAllByTramite_IdAndEstadoTrueOrderByFechaCreacionAsc(Long tramite);

    HistoricoTramiteObservacion findFirstByTramite_idOrderByIdDesc(Long tramite);

}
