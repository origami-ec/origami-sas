package org.ibarra.entity;

import org.ibarra.util.EsquemaConfig;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tarea_notificada_tramite", schema =  EsquemaConfig.procesos)
public class TareaNotificadaTramite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tramite;
    private String idTarea;
    private Boolean estado;
    private String usuarioCreacion;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    private String asunto;
    private String mensaje;
    private String destinatario;
    private String origen;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVista;
    private String usuarioVista;
    private String equipo;
    private String estadoVisto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public String getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(String idTarea) {
        this.idTarea = idTarea;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public Date getFechaVista() {
        return fechaVista;
    }

    public void setFechaVista(Date fechaVista) {
        this.fechaVista = fechaVista;
    }

    public String getUsuarioVista() {
        return usuarioVista;
    }

    public void setUsuarioVista(String usuarioVista) {
        this.usuarioVista = usuarioVista;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getEstadoVisto() {
        return estadoVisto;
    }

    public void setEstadoVisto(String estadoVisto) {
        this.estadoVisto = estadoVisto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TareaNotificadaTramite that = (TareaNotificadaTramite) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TareaNotificadaTramite{" +
                "id=" + id +
                ", tramite=" + tramite +
                ", idTarea='" + idTarea + '\'' +
                ", estado=" + estado +
                ", usuarioCreacion='" + usuarioCreacion + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", asunto='" + asunto + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", destinatario='" + destinatario + '\'' +
                ", origen='" + origen + '\'' +
                ", fechaVista=" + fechaVista +
                ", usuarioVista='" + usuarioVista + '\'' +
                ", equipo='" + equipo + '\'' +
                ", estadoVisto='" + estadoVisto + '\'' +
                '}';
    }
}
