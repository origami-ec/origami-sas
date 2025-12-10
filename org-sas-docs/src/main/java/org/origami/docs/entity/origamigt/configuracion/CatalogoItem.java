package org.origami.docs.entity.origamigt.configuracion;


import java.math.BigDecimal;

public class CatalogoItem {
    private Long id;
    private String codigo;
    private Catalogo catalogo;
    private String descripcion;
    private Boolean defaultValue;
    private String estado;
    private Integer orden;
    private Double porcentaje;
    private Double porcentajeRetencion;
    private Boolean porcentajeLibre;
    private String tipoImpuesto;
    private String texto;
    private CatalogoItem padre;
    private Long referencia;
    private BigDecimal valor;

    public CatalogoItem() {

    }

    public CatalogoItem(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Double getPorcentajeRetencion() {
        return porcentajeRetencion;
    }

    public void setPorcentajeRetencion(Double porcentajeRetencion) {
        this.porcentajeRetencion = porcentajeRetencion;
    }

    public Boolean getPorcentajeLibre() {
        return porcentajeLibre;
    }

    public void setPorcentajeLibre(Boolean porcentajeLibre) {
        this.porcentajeLibre = porcentajeLibre;
    }

    public String getTipoImpuesto() {
        return tipoImpuesto;
    }

    public void setTipoImpuesto(String tipoImpuesto) {
        this.tipoImpuesto = tipoImpuesto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public CatalogoItem getPadre() {
        return padre;
    }

    public void setPadre(CatalogoItem padre) {
        this.padre = padre;
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
