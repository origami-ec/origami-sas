package org.sas.administrativo.repository.talentohumano;

import org.sas.administrativo.entity.talentohumano.ServidorCargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * the interface ServidorCargo Repository
 */
public interface ServidorCargoRepository extends JpaRepository<ServidorCargo, Long> {

    ServidorCargo findByServidor_IdAndEstado(Long servidor, String estado);

    ServidorCargo findTopByServidor_IdOrderByIdDesc(Long servidor);

    ServidorCargo findByServidor_IdAndEstadoIn(Long servidor, List<String> estados);

}
