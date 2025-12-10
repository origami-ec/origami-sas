package org.sas.gateway.repository;

import org.sas.gateway.entity.RuteoModulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuteoModuloRepo extends JpaRepository<RuteoModulo, Long> {

    List<RuteoModulo> findAllByEstadoAndAmbiente(Boolean estado, String ambiente);
}
