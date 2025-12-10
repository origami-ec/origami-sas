package org.ibarra.repository;

import org.ibarra.entity.ParametrizacionProcesos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametrizacionProcesosRepo extends JpaRepository<ParametrizacionProcesos, Long> {

    ParametrizacionProcesos findTopById(Long id);

}