package org.ibarra.dto;

import org.activiti.engine.task.Task;
import org.ibarra.entity.TipoTramite;

import java.util.Date;

public class TareaPendiente {

    private Long id;
    private Long numTramite;
    private String nombrePropietario;
    private HistoricoTramiteDto tramite;
    private TipoTramite idTipoTramite;
    private String taskId;
    private String procInstId;
    private String taskDefKey;
    private String name;
    private String assignee;
    private Date createTime;
    private String formKey;
    private Integer priority;
    private String candidate;
    private Integer rev;
    private Short periodo;
    private Long idTramite;
    private Long tipoTramiteId;
    private PersonaDto persona;
    private Long predio;
    private Long referenciaId;
    private Task task;
    private SolicitudServicioDto solicitudServicio;

    public TareaPendiente() {
    }

    public TareaPendiente(Long id, Long numTramite, String nombrePropietario, HistoricoTramiteDto tramite, TipoTramite idTipoTramite, String taskId, String procInstId, String taskDefKey, String name, String assignee, Date createTime, String formKey, Integer priority, String candidate, Integer rev, Short periodo, Long idTramite, Long tipoTramiteId) {
        this.id = id;
        this.numTramite = numTramite;
        this.nombrePropietario = nombrePropietario;
        this.tramite = tramite;
        this.idTipoTramite = idTipoTramite;
        this.taskId = taskId;
        this.procInstId = procInstId;
        this.taskDefKey = taskDefKey;
        this.name = name;
        this.assignee = assignee;
        this.createTime = createTime;
        this.formKey = formKey;
        this.priority = priority;
        this.candidate = candidate;
        this.rev = rev;
        this.periodo = periodo;
        this.idTramite = idTramite;
        this.tipoTramiteId = tipoTramiteId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public HistoricoTramiteDto getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramiteDto tramite) {
        this.tramite = tramite;
    }

    public TipoTramite getIdTipoTramite() {
        return idTipoTramite;
    }

    public void setIdTipoTramite(TipoTramite idTipoTramite) {
        this.idTipoTramite = idTipoTramite;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Long getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(Long idTramite) {
        this.idTramite = idTramite;
    }

    public Long getTipoTramiteId() {
        return tipoTramiteId;
    }

    public void setTipoTramiteId(Long tipoTramiteId) {
        this.tipoTramiteId = tipoTramiteId;
    }

    public PersonaDto getPersona() {
        return persona;
    }

    public void setPersona(PersonaDto persona) {
        this.persona = persona;
    }

    public Long getPredio() {
        return predio;
    }

    public void setPredio(Long predio) {
        this.predio = predio;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public SolicitudServicioDto getSolicitudServicio() {
        return solicitudServicio;
    }

    public void setSolicitudServicio(SolicitudServicioDto solicitudServicio) {
        this.solicitudServicio = solicitudServicio;
    }

    @Override
    public String toString() {
        return "TareaPendiente{" +
                "id=" + id +
                ", numTramite=" + numTramite +
                ", nombrePropietario='" + nombrePropietario + '\'' +
                ", tramite=" + tramite +
                ", idTipoTramite=" + idTipoTramite +
                ", taskId='" + taskId + '\'' +
                ", procInstId='" + procInstId + '\'' +
                ", taskDefKey='" + taskDefKey + '\'' +
                ", name='" + name + '\'' +
                ", assignee='" + assignee + '\'' +
                ", createTime=" + createTime +
                ", formKey='" + formKey + '\'' +
                ", priority=" + priority +
                ", candidate='" + candidate + '\'' +
                ", rev=" + rev +
                ", periodo=" + periodo +
                ", idTramite=" + idTramite +
                ", tipoTramiteId=" + tipoTramiteId +
                '}';
    }
}
