package org.sas.seguridad.repository;

import org.sas.seguridad.dto.Persona;
import org.sas.seguridad.entity.PersonaFD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<PersonaFD, Long> {
}
