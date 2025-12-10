package org.sas.zull.repository;

import org.sas.zull.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsuario(String usuario);

    @Query("SELECT new org.sas.zull.entity.Usuario(u.usuario, u.clave, u.estado) FROM Usuario u WHERE u.usuario =?1 and u.estado =?2")
    Usuario findByUsuarioAndEstado(String usuario, String estado);

    @Query("SELECT new org.sas.zull.entity.Usuario(u.id, u.usuario, u.estado, u.activeDirectory) FROM Usuario u WHERE u.activeDirectory =?1")
    Usuario findByActiveDirectoryAndEstado(String activeDirectory, String estado);

}
