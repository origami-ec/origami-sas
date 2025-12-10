package org.ibarra.repository;

import org.ibarra.entity.DocumentoTramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoTramiteRepo extends JpaRepository<DocumentoTramite, Long> {

    public List<DocumentoTramite> findAllByClaseNameAndIdReferencia(String className, String idReferencia);

    public List<DocumentoTramite> findAllByClaseNameAndTramite_Id(String className, Long hiTramite);

    DocumentoTramite findTopByClaseNameAndIdReferencia(String className, String idReferencia);

}
