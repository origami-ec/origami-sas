package org.sas.seguridad.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "notificaciones_detalle")
public class NotificacionesDetalle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreUsuario;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaRevision;
    @JoinColumn(name = "notificacion")
    @ManyToOne
    private Notificaciones notificacion;
    private Long persona;
    private String personaLogeada;

    public NotificacionesDetalle() {
    }

    public NotificacionesDetalle(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public Notificaciones getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Notificaciones notificacion) {
        this.notificacion = notificacion;
    }

    public Long getPersona() {
        return persona;
    }

    public void setPersona(Long persona) {
        this.persona = persona;
    }

    public String getPersonaLogeada() {
        return personaLogeada;
    }

    public void setPersonaLogeada(String personaLogeada) {
        this.personaLogeada = personaLogeada;
    }

    @Override
    public String toString() {
        return "NotificacionesDetalle{" +
                "id=" + id +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", fechaRevision=" + fechaRevision +
                ", notificacion=" + notificacion +
                ", persona=" + persona +
                ", personaLogeada='" + personaLogeada + '\'' +
                '}';
    }
}
