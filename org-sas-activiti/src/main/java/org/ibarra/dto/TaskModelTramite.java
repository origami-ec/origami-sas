package org.ibarra.dto;

import org.ibarra.util.Utils;
import org.ibarra.util.model.EstadoTarea;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskModelTramite {

    private String id;
    private String name;
    private String description;
    private int priority;
    private String owner;
    private String assignee;
    private Date dueDate;
    private Date creataDate;
    private Date endDate;
    private String category;
    private String taskDefinitionKey;
    private String parentTaskId;
    private boolean isSuspended;
    private EstadoTarea estado;
    private String descripcionEstado;
    private String color;
    private String textColor;
    private HistoricoTramiteObservacionDto ultimaObservacion;
    private String usuarioModificacion;
    private Date fechaModificacion;
    private String observacion;
    private Long idTramite;
    private List<UsuarioDetalle> usuarios;


    public TaskModelTramite() {
    }

    public TaskModelTramite(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCreataDate() {
        return creataDate;
    }

    public void setCreataDate(Date creataDate) {
        this.creataDate = creataDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public HistoricoTramiteObservacionDto getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(HistoricoTramiteObservacionDto ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(Long idTramite) {
        this.idTramite = idTramite;
    }

    public List<UsuarioDetalle> getUsuarios() {
        if(Utils.isEmpty(usuarios)) {
            usuarios = new ArrayList<>();
        }
        return usuarios;
    }

    public void setUsuarios(List<UsuarioDetalle> usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public String toString() {
        return "TaskModelTramite{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", owner='" + owner + '\'' +
                ", assignee='" + assignee + '\'' +
                ", dueDate=" + dueDate +
                ", creataDate=" + creataDate +
                ", endDate=" + endDate +
                ", category='" + category + '\'' +
                ", taskDefinitionKey='" + taskDefinitionKey + '\'' +
                ", parentTaskId='" + parentTaskId + '\'' +
                ", isSuspended=" + isSuspended +
                ", estado=" + estado +
                ", descripcionEstado='" + descripcionEstado + '\'' +
                ", color='" + color + '\'' +
                ", ultimaObservacion=" + ultimaObservacion +
                ", usuarioModificacion='" + usuarioModificacion + '\'' +
                ", fechaModificacion=" + fechaModificacion +
                ", observacion='" + observacion + '\'' +
                '}';
    }
}
