package org.sas.zull.repository;

import org.sas.zull.entity.UsuarioAcceso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioAccesoRepository extends JpaRepository<UsuarioAcceso, Long> {

    UsuarioAcceso findByUsuarioAndEstado(Long usuario, String estado);

}
