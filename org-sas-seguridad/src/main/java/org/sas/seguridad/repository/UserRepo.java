package org.sas.seguridad.repository;

import org.sas.seguridad.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Usuario, Long>, PagingAndSortingRepository<Usuario, Long> {

    @Query("SELECT new Usuario(u.id,u.usuario, u.dobleVerificacion, u.personaId, u.notificarCorreo) FROM Usuario u WHERE u.usuario = ?1")
    Usuario findByUsuario(String usuario);

    @Query("SELECT new Usuario(u.id,u.usuario, u.dobleVerificacion, u.personaId, u.notificarCorreo,u.estado) FROM Usuario u WHERE u.personaId = ?1 ")
    Usuario findByPersona(Long id);

    Usuario findByPersonaIdAndEstado(Long personaId, String estado);


}
