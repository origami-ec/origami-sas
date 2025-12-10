package org.sas.administrativo.repository;

import org.sas.administrativo.entity.configuracion.SecuenciaGeneral;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface SecuenciaGeneral repo.
 */
public interface SecuenciaGeneralRepository extends JpaRepository<SecuenciaGeneral, Long> {

    SecuenciaGeneral findByCodeAndAnio(String code, Integer anio);
    SecuenciaGeneral findByCode(String code);
}
