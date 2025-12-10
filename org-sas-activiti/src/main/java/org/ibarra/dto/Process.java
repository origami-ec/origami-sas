package org.ibarra.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Process implements Serializable {

    private Long id;
    private TipoTramiteDto tipoTramite;
    private Long tramiteId;
    private String tramite;
    private Long solicitante;
    private String concepto;
    private Long referenciaId;
    private String referencia; //Se puede guardar cualquier ID o informacion pertinente
    private String usuario;
    private String observacion;
    private String usuarioObservacion;
    private Integer periodo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaMaximiFinalizacion;

    private Map<String, Object> parametros;

    public Process() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTramiteId() {
        return tramiteId;
    }

    public void setTramiteId(Long tramiteId) {
        this.tramiteId = tramiteId;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public TipoTramiteDto getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramiteDto tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Map<String, Object> getParametros() {
        if (parametros == null) {
            parametros = new HashMap<>();
        }
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public Long getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Long solicitante) {
        this.solicitante = solicitante;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public String getUsuarioObservacion() {
        return usuarioObservacion;
    }

    public void setUsuarioObservacion(String usuarioObservacion) {
        this.usuarioObservacion = usuarioObservacion;
    }

    public Date getFechaMaximiFinalizacion() {
        return fechaMaximiFinalizacion;
    }

    public void setFechaMaximiFinalizacion(Date fechaMaximiFinalizacion) {
        this.fechaMaximiFinalizacion = fechaMaximiFinalizacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", tipoTramite=" + tipoTramite +
                ", tramiteId=" + tramiteId +
                ", tramite='" + tramite + '\'' +
                ", solicitante=" + solicitante +
                ", concepto='" + concepto + '\'' +
                ", referenciaId=" + referenciaId +
                ", referencia='" + referencia + '\'' +
                ", usuario='" + usuario + '\'' +
                ", parametros=" + parametros +
                '}';
    }
}

