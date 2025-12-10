package org.sas.administrativo.repository.configuracion;

import org.sas.administrativo.entity.configuracion.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepo extends JpaRepository<Reporte, Long> {
    List<Reporte> findReporteByEstado(String estado);

    Reporte findByCodigoSistemaAndEstado(String codigo, String estado);
}
