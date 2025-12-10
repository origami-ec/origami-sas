package org.sas.seguridad.repository;

import org.sas.seguridad.entity.Valor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValorRepo extends JpaRepository<Valor, Long> {

    Valor findByCode(String codigo);

    List<Valor> findByCodeIn(List<String> codigos);

}
