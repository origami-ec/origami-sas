package org.ibarra.entity;

import org.ibarra.util.EsquemaConfig;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_tramite_requisito", schema = EsquemaConfig.procesos)
public class TipoTramiteRequisito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private String formatoArchivo;
    private Boolean requerido;
    @JoinColumn(name = "tipo_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    private TipoTramite tipoTramite;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    private TipoTramiteRequisito padre;
    private String estado;

    public TipoTramiteRequisito() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFormatoArchivo() {
        return formatoArchivo;
    }

    public void setFormatoArchivo(String formatoArchivo) {
        this.formatoArchivo = formatoArchivo;
    }

    public Boolean getRequerido() {
        return requerido;
    }

    public void setRequerido(Boolean requerido) {
        this.requerido = requerido;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public TipoTramiteRequisito getPadre() {
        return padre;
    }

    public void setPadre(TipoTramiteRequisito padre) {
        this.padre = padre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
