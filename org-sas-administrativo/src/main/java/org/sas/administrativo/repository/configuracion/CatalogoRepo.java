package org.sas.administrativo.repository.configuracion;

import org.sas.administrativo.entity.configuracion.Catalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Catalogo repo.
 */
@Repository
public interface CatalogoRepo extends JpaRepository<Catalogo, Long> {
    
}
