package org.ibarra.entity;

import org.ibarra.util.EsquemaConfig;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "historico_tramite_observacion", schema = EsquemaConfig.procesos)
public class HistoricoTramiteObservacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String observacion;
    private String tarea;
    private Boolean estado;
    private String usuarioCreacion;
    private Date fechaCreacion;
    @JoinColumn(name = "tramite", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private HistoricoTramite tramite;
    private String observacionPublica;

    public HistoricoTramiteObservacion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
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

    public HistoricoTramite getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramite tramite) {
        this.tramite = tramite;
    }

    public String getObservacionPublica() {
        return observacionPublica;
    }

    public void setObservacionPublica(String observacionPublica) {
        this.observacionPublica = observacionPublica;
    }
}
