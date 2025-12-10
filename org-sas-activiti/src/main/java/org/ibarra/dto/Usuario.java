package org.ibarra.dto;

import jakarta.persistence.*;
import org.ibarra.entity.Persona;

import java.io.Serializable;
import java.util.Date;

public class Usuario implements Serializable {

    private Long id;
    private String usuario;
    private String estado;
    private String codigo;
    private Boolean notificarCorreo;
    private Long personaId;
    private Persona persona;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }

    public Boolean getNotificarCorreo() {
        if (notificarCorreo == null) {
            notificarCorreo = Boolean.FALSE;
        }
        return notificarCorreo;
    }

    public void setNotificarCorreo(Boolean notificarCorreo) {
        this.notificarCorreo = notificarCorreo;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
