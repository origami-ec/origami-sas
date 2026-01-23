package org.sas.administrativo.repository.configuracion;

import org.sas.administrativo.entity.configuracion.Parroquia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParroquiaRepository extends JpaRepository<Parroquia, Long> {

    List<Parroquia> findAllByCodigoAndEstado(String codigo, String estado);
    @Query("select p from Parroquia p where p.codigo=:codigo and p.estado=:estado and p.id <> :id")
    List<Parroquia> buscarCodigoAndEstadoAndNotId(String codigo,String estado,Long id);

}
