package org.ibarra.dto;

import org.ibarra.util.model.EstadoTarea;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Tarea implements Serializable {

    private String taskId;
    private String taskDefinitionKey;
    private String taskName;
    private String assignee;

    private int priority;
    private String processInstanceID;
    private String formKey;
    private Date fechaInicio;
    private Date fechaFin;

    //campos para completar tarea
    private String observacion;
    private String observacionUsuario;
    private String observacionPublica;
    private String detalle;
    private EstadoTarea estado;
    private Boolean notificacionAsignacion;
    private Integer version;
    private Integer latestVersion;
    private String tipo;

    //campos multiinstancia
    private Boolean multiTask;
    private Integer totalInstancias;
    private Integer instanciasCompletadas;
    private Integer instanciasRestantes;


    //campos de informacion de tarea
    private HistoricoTramiteDto tramite;
    private List<HistoricoTramiteObservacionDto> observaciones;
    private Map<String, Object> parametros;
    private Map<String, Object> parametrosTarea;

    public Tarea() {
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getProcessInstanceID() {
        return processInstanceID;
    }

    public void setProcessInstanceID(String processInstanceID) {
        this.processInstanceID = processInstanceID;
    }

    public HistoricoTramiteDto getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramiteDto tramite) {
        this.tramite = tramite;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public List<HistoricoTramiteObservacionDto> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(List<HistoricoTramiteObservacionDto> observaciones) {
        this.observaciones = observaciones;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getObservacionUsuario() {
        return observacionUsuario;
    }

    public void setObservacionUsuario(String observacionUsuario) {

        this.observacionUsuario = observacionUsuario;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }

    public Boolean getNotificacionAsignacion() {
        return notificacionAsignacion;
    }

    public void setNotificacionAsignacion(Boolean notificacionAsignacion) {
        this.notificacionAsignacion = notificacionAsignacion;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(Integer latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Map<String, Object> getParametrosTarea() {
        return parametrosTarea;
    }

    public void setParametrosTarea(Map<String, Object> parametrosTarea) {
        this.parametrosTarea = parametrosTarea;
    }

    public Integer getTotalInstancias() {
        return totalInstancias;
    }

    public void setTotalInstancias(Integer totalInstancias) {
        this.totalInstancias = totalInstancias;
    }

    public Integer getInstanciasCompletadas() {
        return instanciasCompletadas;
    }

    public void setInstanciasCompletadas(Integer instanciasCompletadas) {
        this.instanciasCompletadas = instanciasCompletadas;
    }

    public Integer getInstanciasRestantes() {
        return instanciasRestantes;
    }

    public void setInstanciasRestantes(Integer instanciasRestantes) {
        this.instanciasRestantes = instanciasRestantes;
    }

    public Boolean getMultiTask() {
        return multiTask;
    }

    public void setMultiTask(Boolean multiTask) {
        this.multiTask = multiTask;
    }

    public String getObservacionPublica() {
        return observacionPublica;
    }

    public void setObservacionPublica(String observacionPublica) {
        this.observacionPublica = observacionPublica;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "taskId='" + taskId + '\'' +
                ", taskDefinitionKey='" + taskDefinitionKey + '\'' +
                ", taskName='" + taskName + '\'' +
                ", assignee='" + assignee + '\'' +
                ", priority=" + priority +
                ", processInstanceID='" + processInstanceID + '\'' +
                ", formKey='" + formKey + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", observacion='" + observacion + '\'' +
                ", observacionUsuario='" + observacionUsuario + '\'' +
                ", detalle='" + detalle + '\'' +
                ", estado=" + estado +
                ", notificacionAsignacion=" + notificacionAsignacion +
                ", version=" + version +
                ", latestVersion=" + latestVersion +
                ", tipo='" + tipo + '\'' +
                ", multiTask=" + multiTask +
                ", totalInstancias=" + totalInstancias +
                ", instanciasCompletadas=" + instanciasCompletadas +
                ", instanciasRestantes=" + instanciasRestantes +
                ", tramite=" + tramite +
                ", observaciones=" + observaciones +
                ", parametros=" + parametros +
                ", parametrosTarea=" + parametrosTarea +
                '}';
    }
}
