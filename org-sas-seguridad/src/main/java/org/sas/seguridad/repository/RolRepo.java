package org.sas.seguridad.repository;

import org.sas.seguridad.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolRepo extends JpaRepository<Rol, Long> {
    @Query(value = "SELECT ur.rol FROM UsuarioRol ur WHERE ur.usuario.id = :idUsuario AND ur.rol.estado = true AND ur.usuario.estado = 'ACTIVO'")
    List<Rol> getRolesByUsuario(Long idUsuario);
}
