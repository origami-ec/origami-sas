package org.sas.seguridad.repository;

import org.sas.seguridad.entity.UsuarioClave;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioClaveRepo extends JpaRepository<UsuarioClave, Long> {

    UsuarioClave findByUsuarioAndClaveAndEstado(Long usuario, String clave, Boolean estado);
}
