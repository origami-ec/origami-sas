package org.sas.seguridad.repository;


import org.sas.seguridad.entity.RuteoModulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuteoModuloRepo extends JpaRepository<RuteoModulo, Long> {

}
