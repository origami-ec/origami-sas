package org.sas.seguridad.repository;

import org.sas.seguridad.entity.Entidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntidadRepo extends JpaRepository<Entidad, Long> {
}
