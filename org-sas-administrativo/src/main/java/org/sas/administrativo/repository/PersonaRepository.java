package org.sas.administrativo.repository;

import org.sas.administrativo.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Persona findTop1ByNumIdentificacionOrderByIdDesc(String numIdentificacion);

    @Query(value = "SELECT " +
            "new org.sas.administrativo.entity.Persona(" +
            "p.id, p.numIdentificacion, p.nombre, p.apellido, p.domicilio, p.correo, p.telefono, \n" +
            "p.celular,   p.estado, p.estadoCivil,  \n" +
            "  p.condicionCiudadano, p.representanteLegal,   \n" +
            "   p.fechaNacimiento ) FROM Persona p WHERE p.id = ?1")
    Persona findByIdPersona(Long id);
}
