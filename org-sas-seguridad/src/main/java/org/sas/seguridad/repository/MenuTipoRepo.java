package org.sas.seguridad.repository;

import org.sas.seguridad.entity.MenuTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuTipoRepo extends JpaRepository<MenuTipo, Integer> {



}
