package org.sas.seguridad.dto;

import java.util.Date;

public class UsuarioVerificacionDTO {

    private Long id;
    private UsuarioInicioSesion usuario;
    private Long codigoVerificacion;
    private Date fecha;
    private Boolean estado;

    private String codigoError;
    private String mensaje;

    public UsuarioVerificacionDTO() {
    }

    public UsuarioVerificacionDTO(Long id, String codigoError, String mensaje) {
        this.id = id;
        this.codigoError = codigoError;
        this.mensaje = mensaje;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioInicioSesion getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioInicioSesion usuario) {
        this.usuario = usuario;
    }

    public Long getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(Long codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
