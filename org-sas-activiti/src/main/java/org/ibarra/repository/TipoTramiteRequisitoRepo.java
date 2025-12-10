package org.ibarra.repository;

import org.ibarra.entity.TipoTramiteRequisito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoTramiteRequisitoRepo extends JpaRepository<TipoTramiteRequisito, Long> {

    List<TipoTramiteRequisito> findAllByTipoTramite_IdAndEstado(Long tipoTramite, String estado);

    List<TipoTramiteRequisito> findAllByPadre_Id(Long tipoTramiteRequisito);

}
