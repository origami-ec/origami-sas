package org.sas.administrativo.entity.configuracion;


import org.sas.administrativo.util.EsquemaConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The type Catalogo item.
 */
@Entity
@Table(name = "catalogo_item", schema = EsquemaConfig.configuracion)
public class CatalogoItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "codigo")
    private String codigo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogo", referencedColumnName = "id")
    private Catalogo catalogo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "default_value")
    private Boolean defaultValue;
    @Column(name = "estado")
    private String estado;
    @Column(name = "orden")
    private Integer orden;
    @Column(name = "porcentaje")
    private Double porcentaje;
    @Column(name = "porcentaje_retencion")
    private Double porcentajeRetencion;
    @Column(name = "porcentaje_libre")
    private Boolean porcentajeLibre;
    @Column(name = "tipo_impuesto")
    private String tipoImpuesto;
    @Column(name = "texto")
    private String texto;
    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre", referencedColumnName = "id")
    private CatalogoItem padre;*/
    @Column(name = "referencia")
    private Long referencia;
    @Column(name = "valor", precision = 19, scale = 4)
    private BigDecimal valor;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "usuario_create")
    private String usuarioCreate;
    @Column(name = "usuario_edit")
    private String usuarioEdit;
    @Column(name = "usuario_delete")
    private String usuarioDelete;
    @Column(name = "fecha_create")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreate;
    @Column(name = "fecha_edit")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdit;
    @Column(name = "fecha_delete")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDelete;
    private String tipo;


    public CatalogoItem() {

    }

    public CatalogoItem(Long id) {
        this.id = id;
    }

    public CatalogoItem(String codigo) {
        this.codigo = codigo;
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getUsuarioCreate() {
        return usuarioCreate;
    }

    public void setUsuarioCreate(String usuarioCreate) {
        this.usuarioCreate = usuarioCreate;
    }

    public String getUsuarioEdit() {
        return usuarioEdit;
    }

    public void setUsuarioEdit(String usuarioEdit) {
        this.usuarioEdit = usuarioEdit;
    }

    public String getUsuarioDelete() {
        return usuarioDelete;
    }

    public void setUsuarioDelete(String usuarioDelete) {
        this.usuarioDelete = usuarioDelete;
    }

    public Date getFechaCreate() {
        return fechaCreate;
    }

    public void setFechaCreate(Date fechaCreate) {
        this.fechaCreate = fechaCreate;
    }

    public Date getFechaEdit() {
        return fechaEdit;
    }

    public void setFechaEdit(Date fechaEdit) {
        this.fechaEdit = fechaEdit;
    }

    public Date getFechaDelete() {
        return fechaDelete;
    }

    public void setFechaDelete(Date fechaDelete) {
        this.fechaDelete = fechaDelete;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "CatalogoItem{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", catalogo=" + catalogo +
                ", descripcion='" + descripcion + '\'' +
                ", defaultValue=" + defaultValue +
                ", estado='" + estado + '\'' +
                ", orden=" + orden +
                ", porcentaje=" + porcentaje +
                ", porcentajeRetencion=" + porcentajeRetencion +
                ", porcentajeLibre=" + porcentajeLibre +
                ", tipoImpuesto='" + tipoImpuesto + '\'' +
                ", texto='" + texto + '\'' +

                ", referencia=" + referencia +
                ", valor=" + valor +
                ", cantidad=" + cantidad +
                ", usuarioCreate='" + usuarioCreate + '\'' +
                ", usuarioEdit='" + usuarioEdit + '\'' +
                ", usuarioDelete='" + usuarioDelete + '\'' +
                ", fechaCreate=" + fechaCreate +
                ", fechaEdit=" + fechaEdit +
                ", fechaDelete=" + fechaDelete +
                '}';
    }
}