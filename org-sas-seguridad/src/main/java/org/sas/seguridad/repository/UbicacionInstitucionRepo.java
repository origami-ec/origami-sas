package org.sas.seguridad.repository;

import org.sas.seguridad.entity.UbicacionInstitucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionInstitucionRepo extends JpaRepository<UbicacionInstitucion, Long> {
}
