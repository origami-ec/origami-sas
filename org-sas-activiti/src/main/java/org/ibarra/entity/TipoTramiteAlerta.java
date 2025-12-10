package org.ibarra.entity;

import org.ibarra.util.EsquemaConfig;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tipo_tramite_alerta", schema = EsquemaConfig.procesos)
public class TipoTramiteAlerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String estado;
    private Boolean activarNotificacion;
    private Integer tiempoActivacionPrevia;
    private Boolean repetirNotificacion;
    private Integer tiempoRepetirNotificacion;
    private String usuarioCreacion;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    private String usuarioModificacion;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @JoinColumn(name = "tipo_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoTramite tipoTramite;


    public TipoTramiteAlerta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getActivarNotificacion() {
        return activarNotificacion;
    }

    public void setActivarNotificacion(Boolean activarNotificacion) {
        this.activarNotificacion = activarNotificacion;
    }

    public Integer getTiempoActivacionPrevia() {
        return tiempoActivacionPrevia;
    }

    public void setTiempoActivacionPrevia(Integer tiempoActivacionPrevia) {
        this.tiempoActivacionPrevia = tiempoActivacionPrevia;
    }

    public Boolean getRepetirNotificacion() {
        return repetirNotificacion;
    }

    public void setRepetirNotificacion(Boolean repetirNotificacion) {
        this.repetirNotificacion = repetirNotificacion;
    }

    public Integer getTiempoRepetirNotificacion() {
        return tiempoRepetirNotificacion;
    }

    public void setTiempoRepetirNotificacion(Integer tiempoRepetirNotificacion) {
        this.tiempoRepetirNotificacion = tiempoRepetirNotificacion;
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

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tramite) {
        this.tipoTramite = tramite;
    }

    @Override
    public String toString() {
        return "TipoTramiteAlerta{" +
                "id=" + id +
                ", estado='" + estado + '\'' +
                ", activarNotificacion=" + activarNotificacion +
                ", tiempoActivacionPrevia=" + tiempoActivacionPrevia +
                ", repetirNotificacion=" + repetirNotificacion +
                ", tiempoRepetirNotificacion=" + tiempoRepetirNotificacion +
                ", usuarioCreacion='" + usuarioCreacion + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", usuarioModificacion='" + usuarioModificacion + '\'' +
                ", fechaModificacion=" + fechaModificacion +
                '}';
    }
}
