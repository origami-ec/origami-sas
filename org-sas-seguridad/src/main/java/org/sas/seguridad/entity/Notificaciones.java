package org.sas.seguridad.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "notificaciones")
public class Notificaciones implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String motivo;
    private String usuarioIngreso;
    private String estado;
    private Date fechaIngreso;

    public Notificaciones() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }


    @Override
    public String toString() {
        return "Notificaciones{" +
                ", id=" + id +
                ", motivo='" + motivo + '\'' +
                ", usuarioIngreso='" + usuarioIngreso + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}
