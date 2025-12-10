package org.sas.seguridad.dto;

public class DocumentoTramiteDto {
    private Long id;
    private HistoricoTramiteDto tramite;
    private TipoTramiteRequisitoDto requisito;
    private String descripcion;
    private String referenciaDoc;
    private String claseName;
    private String idReferencia;

    public DocumentoTramiteDto(){

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
