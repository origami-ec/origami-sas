package org.ibarra.repository;

import org.ibarra.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Persona findByNumIdentificacion(String numIdentificacion);

    Persona findByIdAndEstado(Long id, Boolean estado);

}
