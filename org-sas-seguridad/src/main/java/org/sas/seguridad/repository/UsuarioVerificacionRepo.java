package org.sas.seguridad.repository;

import org.sas.seguridad.entity.UsuarioVerificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioVerificacionRepo extends JpaRepository<UsuarioVerificacion, Long> {
}
