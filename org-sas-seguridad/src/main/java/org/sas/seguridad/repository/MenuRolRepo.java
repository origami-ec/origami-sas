package org.sas.seguridad.repository;

import org.sas.seguridad.entity.MenuRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRolRepo extends JpaRepository<MenuRol, Long> {

    @Query(value = "delete from menu_rol u where u.id = ?1", nativeQuery = true)
    Integer deleteXid(Long id);


    List<MenuRol> findAllByRol(Long rol);

    @Query("SELECT mr FROM MenuRol mr " +
            "WHERE mr.menu.menuPadre is null and " +
            "mr.rol = ?1 " +
            "ORDER BY mr.menu.tipo.id ASC,  mr.menu.numPosicion ASC")
    List<MenuRol> findAllByRolAndMenuPadreIsNull(Long rol);

    @Query("SELECT distinct mr FROM MenuRol mr " +
            "WHERE mr.menu.menuPadre is null and " +
            "mr.rol in ?1 " +
            "ORDER BY mr.menu.numPosicion ASC")
    List<MenuRol> findAllMenuPadreByRoles(List<Long> roles);

}
