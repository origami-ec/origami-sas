package org.sas.administrativo.repository;

import org.sas.administrativo.entity.FirmaDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FirmaDocumentoRepository extends JpaRepository<FirmaDocumento, Long> {

    List<FirmaDocumento> findAllByReferenciaAndTipo_IdAndEstado_IdIn(Long referencia, Long tipo, List<Long> estado);

    List<FirmaDocumento> findAllByReferenciaAndTipo_IdOrderByIdDesc(Long referencia, Long tipo);

    List<FirmaDocumento> findAllByServidorAndEstado_IdOrderByIdDesc(Long servidor, Long estado);

    List<FirmaDocumento> findAllByReferenciaAndTipo_CodigoAndEstado_Codigo(Long referencia, String tipo, String estado);

    List<FirmaDocumento> findByServidorAndEstado_CodigoAndTipo_CodigoAndReferencia(Long servidor, String estadoFirma, String tipoFirma, Long referencia);

    List<FirmaDocumento> findByEstado_CodigoAndTipo_CodigoAndReferencia(String estadoFirma, String tipoFirma, Long referencia);

    List<FirmaDocumento> findByServidorAndEstado_CodigoAndTipo_CodigoAndTramite(Long servidor, String estadoFirma, String tipoFirma, String tramite);

    Integer countByServidorAndEstado_IdAndDetalleIsNull(Long servidor, Long estado);
}

