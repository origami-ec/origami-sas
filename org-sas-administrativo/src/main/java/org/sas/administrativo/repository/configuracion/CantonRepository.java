package org.sas.administrativo.repository.configuracion;


import org.sas.administrativo.entity.Canton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CantonRepository extends JpaRepository<Canton, Long> {
    List<Canton> findAllByEstado(String estado);

    List<Canton> findAllByCodigoAndEstado(String codigo, String estado);

    @Query("select c from Canton c where c.codigo=:codigo and c.estado=:estado and c.id<>:id")
    List<Canton> buscarCodigoAndEstadoAndNotId(String codigo, String estado, Long id);
}
