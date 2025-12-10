package org.sas.seguridad.dto;

import org.sas.seguridad.entity.Tramite;

import java.io.Serializable;

public class TipoTramiteDto implements Serializable {

    private Long id;
    private String descripcion;
    private String activitykey;
    private Boolean estado;
    private String archivoBpmn;
    private String abreviatura;

    private Tramite tramite;

    public TipoTramiteDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActivitykey() {
        return activitykey;
    }

    public void setActivitykey(String activitykey) {
        this.activitykey = activitykey;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getArchivoBpmn() {
        return archivoBpmn;
    }

    public void setArchivoBpmn(String archivoBpmn) {
        this.archivoBpmn = archivoBpmn;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Tramite getTramite() {
        return tramite;
    }

    public void setTramite(Tramite tramite) {
        this.tramite = tramite;
    }

    @Override
    public String toString() {
        return "TipoTramite{" + "id=" + id + ", descripcion='" + descripcion + '\'' + ", activitykey='" + activitykey
                + '\'' + ", estado=" + estado + ", archivoBpmn='" + archivoBpmn + '\'' + ", abreviatura='" + abreviatura+ '\''
                + ", tramite=" + tramite + '}';
    }
}
