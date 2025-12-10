package org.ibarra.dto;


public class TipoTramiteRequisitoDto {
    private Long id;
    private String descripcion;
    private String formatoArchivo;
    private Boolean requerido;
    private TipoTramiteDto tipoTramite;
    private TipoTramiteRequisitoDto padre;
    private String estado;

    public TipoTramiteRequisitoDto() {

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

    public TipoTramiteDto getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramiteDto tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public TipoTramiteRequisitoDto getPadre() {
        return padre;
    }

    public void setPadre(TipoTramiteRequisitoDto padre) {
        this.padre = padre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "TipoTramiteRequisitoDto{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", formatoArchivo='" + formatoArchivo + '\'' +
                ", requerido=" + requerido +
                ", tipoTramite=" + tipoTramite +
                '}';
    }
}
