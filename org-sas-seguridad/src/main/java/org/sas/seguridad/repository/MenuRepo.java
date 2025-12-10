package org.sas.seguridad.repository;

import org.sas.seguridad.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepo extends JpaRepository<Menu, Integer>, PagingAndSortingRepository<Menu, Integer> {

    @Query("SELECT m FROM Menu m WHERE m.menuPadre is null AND  m.tipo.id = 1 ORDER BY m.tipo.id ASC, m.numPosicion ASC")
    List<Menu> findAllByMenuPadreIsNull();

    @Query("SELECT distinct m FROM Menu m WHERE m.menuPadre.id = ?1 " +
            "AND (m.tipo.id = 1 or  m.tipo.id = 3) " +
            "ORDER BY m.numPosicion ASC")
    List<Menu> findAllMenusByMenuPadre(Integer padre);

    @Query("SELECT distinct m FROM Menu m WHERE m.tipo.id = 3 AND m.menuPadre is null")
    List<Menu> findMenusPadresPublic();

    @Query("SELECT distinct mr.menu FROM MenuRol mr  " +
            "WHERE mr.rol in ?1 and mr.menu.tipo.id = 1 " +
            "and mr.menu.menuPadre is null " +
            "ORDER BY mr.menu.numPosicion ASC")
    List<Menu> findByMenusPadresXroles(List<Long> roles);

    @Query("SELECT mr.menu FROM MenuRol mr  " +
            "WHERE mr.menu.menuPadre.id = ?1 and " +
            "mr.rol = ?2 and mr.menu.tipo.id = 1 " +
            "ORDER BY mr.menu.numPosicion ASC")
    List<Menu> findAllByMenuPadreXrol(Integer menuPadre, Long rol);

    @Query("SELECT distinct mr.menu FROM MenuRol mr  " +
            "WHERE mr.menu.menuPadre.id = ?1 and mr.rol in ?2 and mr.menu.tipo.id = 1 " +
            "ORDER BY mr.menu.numPosicion ASC")
    List<Menu> findAllByMenuPadreXroles(Integer menuPadre, List<Long> roles);

    @Query("SELECT distinct me FROM Menu me " +
            "WHERE me.menuPadre.id = ?1 and me.tipo.id = 3 " +
            "ORDER BY me.numPosicion ASC")
    List<Menu> findMenuPublicByMenuPadre(Integer menuPadre);

}
