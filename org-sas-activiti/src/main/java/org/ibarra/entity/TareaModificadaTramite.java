package org.ibarra.entity;

import org.ibarra.util.EsquemaConfig;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tarea_modificada_tramite", schema =  EsquemaConfig.procesos)
public class TareaModificadaTramite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idTarea;
    private Boolean estado;
    private String usuarioCreacion;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    private String observacion;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDuadateAnterior;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDuedate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaDuadateAnterior() {
        return fechaDuadateAnterior;
    }

    public void setFechaDuadateAnterior(Date fechaDuadateAnterior) {
        this.fechaDuadateAnterior = fechaDuadateAnterior;
    }

    public Date getFechaDuedate() {
        return fechaDuedate;
    }

    public void setFechaDuedate(Date fechaDuedate) {
        this.fechaDuedate = fechaDuedate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TareaModificadaTramite that = (TareaModificadaTramite) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TareaModificadaTramite{" +
                "id=" + id +
                ", idTarea='" + idTarea + '\'' +
                ", estado=" + estado +
                ", usuarioCreacion='" + usuarioCreacion + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", observacion='" + observacion + '\'' +
                ", fechaDuadateAnterior=" + fechaDuadateAnterior +
                ", fechaDuedate=" + fechaDuedate +
                '}';
    }
}
