package org.sas.administrativo.repository.talentohumano;

import org.sas.administrativo.entity.talentohumano.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * the interface Servidor Repository
 */
@Repository
public interface ServidorRepository extends JpaRepository<Servidor, Long> {

    Servidor findByPersona_Id(Long idPersona);

    List<Servidor> findAllByEstado(String estado);

    List<Servidor> findTop10ByEstado(String estado);

    Optional<Servidor> getServidorByPersona_NumIdentificacion(String numIdentificacion);

    @Query(value = "select s.id\n" +
            "            from talento_humano.servidor s\n" +
            "            inner join persona ps on ps.id = s.persona\n" +
            "            inner join seguridad_public.usuario u on u.persona = ps.id\n" +
            "            where u.usuario = :user", nativeQuery = true)
    Long getIdServidroByUsuario(String user);

}
