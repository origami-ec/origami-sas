package org.sas.seguridad.repository;

import org.sas.seguridad.entity.Usuario;
import org.sas.seguridad.entity.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRolRepo extends JpaRepository<UsuarioRol, Long>, PagingAndSortingRepository<UsuarioRol, Long> {

    @Query("SELECT r.rol.id FROM UsuarioRol  r WHERE r.usuario.usuario = ?1")
    List<Long> getRolesByUser(String usuario);

    @Query("SELECT r.rol.nombre FROM UsuarioRol  r WHERE r.usuario.usuario = ?1")
    List<String> rolesXUsuario(String usuario);

    @Query("SELECT r.usuario FROM UsuarioRol r WHERE Upper(r.rol.nombre) = upper(?1) AND r.usuario.estado = 'ACTIVO'")
    List<Usuario> consultarUsuarioXrol(String rol);

    @Query("SELECT r FROM UsuarioRol  r WHERE r.usuario.usuario = ?1 AND r.rol.nombre = ?2")
    UsuarioRol usuarioXrol(String usuario, String rol);

}
