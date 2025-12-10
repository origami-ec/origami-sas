package org.sas.seguridad.repository;

import org.sas.seguridad.entity.UsuarioAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioAccesoRepo extends JpaRepository<UsuarioAcceso, Long> {
}
