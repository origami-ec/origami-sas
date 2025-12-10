package org.sas.zull.dto;

import java.io.Serializable;

public class UsuarioInicioSesion implements Serializable {

    private Long id;
    private Long persona;
    private Object servidor;
    private String usuario;
    private String activeDirectory;
    private String clave;
    private String estado;
    private Boolean dobleVerificacion;
    private String correo;
    private String correoEncriptado;
    private String frase;
    private ActiveDirectoryModel activeDirectoryResponse;
    private Boolean notificarCorreo;

    public UsuarioInicioSesion() {
    }

    public String getCorreoEncriptado() {
        return correoEncriptado;
    }

    public void setCorreoEncriptado(String correoEncriptado) {
        this.correoEncriptado = correoEncriptado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

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

    public String getActiveDirectory() {
        return activeDirectory;
    }

    public void setActiveDirectory(String activeDirectory) {
        this.activeDirectory = activeDirectory;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getDobleVerificacion() {
        return dobleVerificacion;
    }

    public void setDobleVerificacion(Boolean dobleVerificacion) {
        this.dobleVerificacion = dobleVerificacion;
    }

    public Long getPersona() {
        return persona;
    }

    public void setPersona(Long persona) {
        this.persona = persona;
    }

    public Object getServidor() {
        return servidor;
    }

    public void setServidor(Object servidor) {
        this.servidor = servidor;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public ActiveDirectoryModel getActiveDirectoryResponse() {
        return activeDirectoryResponse;
    }

    public void setActiveDirectoryResponse(ActiveDirectoryModel activeDirectoryResponse) {
        this.activeDirectoryResponse = activeDirectoryResponse;
    }

    public Boolean getNotificarCorreo() {
        return notificarCorreo;
    }

    public void setNotificarCorreo(Boolean notificarCorreo) {
        this.notificarCorreo = notificarCorreo;
    }

    @Override
    public String toString() {
        return "UsuarioInicioSesion{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", clave='" + clave + '\'' +
                ", estado='" + estado + '\'' +
                ", dobleVerificacion=" + dobleVerificacion +
                ", correo='" + correo + '\'' +
                ", correoEncriptado='" + correoEncriptado + '\'' +
                '}';
    }
}
