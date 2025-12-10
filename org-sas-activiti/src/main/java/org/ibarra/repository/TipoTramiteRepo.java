package org.ibarra.repository;

import org.ibarra.entity.TipoTramite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoTramiteRepo extends JpaRepository<TipoTramite, Long> {
    List<TipoTramite> findAllByActivitykeyInAndEstadoOrderByIdAsc(String[] activitiKeys,Boolean estado);

    List<TipoTramite> findAllByEstadoOrderByIdDesc(Boolean estado);

    TipoTramite findByAbreviaturaAndEstado(String abreviatura, Boolean estado);
}
