package org.ibarra.dto;

import org.ibarra.util.model.EstadoTramite;

import java.util.Date;

public class Tramites {

    private Long id;
    private String numTramite;
    private HistoricoTramiteDto tramite;
    private String procInstId;
    private String procDefId;
    private Date startTime;
    private Date endTime;
    private String deleteReason;
    private String duration;
    private String callProcInstId;
    private String participants;
    private Short periodo;
    private String usercre;
    private EstadoTramite estado;
    private String detalle;

    public void TramiteDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public HistoricoTramiteDto getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramiteDto tramite) {
        this.tramite = tramite;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCallProcInstId() {
        return callProcInstId;
    }

    public void setCallProcInstId(String callProcInstId) {
        this.callProcInstId = callProcInstId;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public String getUsercre() {
        return usercre;
    }

    public void setUsercre(String usercre) {
        this.usercre = usercre;
    }

    public EstadoTramite getEstado() {
        return estado;
    }

    public void setEstado(EstadoTramite estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "TramiteDto{" +
                "id=" + id +
                ", numTramite='" + numTramite + '\'' +
                ", tramite=" + tramite +
                ", procInstId='" + procInstId + '\'' +
                ", procDefId='" + procDefId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", deleteReason='" + deleteReason + '\'' +
                ", duration='" + duration + '\'' +
                ", callProcInstId='" + callProcInstId + '\'' +
                ", participants='" + participants + '\'' +
                ", periodo=" + periodo +
                ", usercre='" + usercre + '\'' +
                '}';
    }
}
