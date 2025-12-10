package org.sas.administrativo.repository.talentohumano;

import org.sas.administrativo.entity.talentohumano.PersonaActividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaActividadRepository extends JpaRepository<PersonaActividad, Long> {

    PersonaActividad findFirstByPersonaAndActividadGeneralAndEstado(Long persona, String actividadGeneral, String estado);
}
