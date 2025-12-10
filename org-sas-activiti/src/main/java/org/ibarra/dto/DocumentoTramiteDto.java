package org.ibarra.dto;

import java.util.Date;
import java.util.List;

public class DocumentoTramiteDto {
    private Long id;
    private HistoricoTramiteDto tramite;
    private TipoTramiteRequisitoDto requisito;
    private String descripcion;
    private String referenciaDoc;
    private String claseName;
    private String idReferencia;

    private String usuarioRegistro;
    private Date fechaRegistro;
    private Long hiTramite;
    private List<FirmaDocumentoDto> documentosParaFirma;

    public  DocumentoTramiteDto(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public TipoTramiteRequisitoDto getRequisito() {
        return requisito;
    }

    public void setRequisito(TipoTramiteRequisitoDto requisito) {
        this.requisito = requisito;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getReferenciaDoc() {
        return referenciaDoc;
    }

    public void setReferenciaDoc(String referenciaDoc) {
        this.referenciaDoc = referenciaDoc;
    }

    public String getClaseName() {
        return claseName;
    }

    public void setClaseName(String claseName) {
        this.claseName = claseName;
    }

    public String getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(String idReferencia) {
        this.idReferencia = idReferencia;
    }

    public HistoricoTramiteDto getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramiteDto tramite) {
        this.tramite = tramite;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public Long getHiTramite() {
        return hiTramite;
    }

    public void setHiTramite(Long hiTramite) {
        this.hiTramite = hiTramite;
    }

    public List<FirmaDocumentoDto> getDocumentosParaFirma() {
        return documentosParaFirma;
    }

    public void setDocumentosParaFirma(List<FirmaDocumentoDto> documentosParaFirma) {
        this.documentosParaFirma = documentosParaFirma;
    }

    @Override
    public String toString() {
        return "DocumentoTramiteDto{" +
                "id=" + id +
                ", requisito=" + requisito +
                ", descripcion='" + descripcion + '\'' +
                ", referenciaDoc='" + referenciaDoc + '\'' +
                ", claseName='" + claseName + '\'' +
                ", idReferencia='" + idReferencia + '\'' +
                '}';
    }
}
