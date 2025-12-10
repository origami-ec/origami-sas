package org.sas.seguridad.repository;

import org.sas.seguridad.entity.CorreoFormato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorreoFormatoRepo extends JpaRepository<CorreoFormato, Long> {

    CorreoFormato findByCodigoAndEstado(String codigo, Boolean estado);

}
