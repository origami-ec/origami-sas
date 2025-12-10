package org.sas.seguridad.repository;

import org.sas.seguridad.entity.MesaAyuda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesaAyudaRepository extends JpaRepository<MesaAyuda, Long>, PagingAndSortingRepository<MesaAyuda, Long> {
    @Query("Select coalesce(max(mesa.id),0) from MesaAyuda mesa")
    Integer getMaxMesaAyuda();

    List<MesaAyuda> findAllByEstado(String estado);

    MesaAyuda findMesaAyudaById(Long id);
}
