package org.ibarra.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UsuarioTarea implements Serializable {
    private String usuario;
    private String processInstanceId;
    private String tarea;
    private String state;
    private String id;

    public UsuarioTarea() {
    }

    public UsuarioTarea(String usuario, String processInstanceId) {
        this.usuario = usuario;
        this.processInstanceId = processInstanceId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
