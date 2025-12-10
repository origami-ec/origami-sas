package org.sas.seguridad.repository;

import org.sas.seguridad.entity.CorreoDominio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorreoDominioRepo extends JpaRepository<CorreoDominio, Long> {
}
