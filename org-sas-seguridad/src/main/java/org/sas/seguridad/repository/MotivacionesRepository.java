package org.sas.seguridad.repository;

import org.sas.seguridad.entity.Motivaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotivacionesRepository extends JpaRepository<Motivaciones, Integer> {
}
