package org.origami.docs.entity.origamigt.talentoHumano;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class Rubro {

    private Long id;
    private String nombre;
    private BigDecimal valor;
    private Boolean porcentaje;
    private String estado;
    private Boolean ingreso;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaActualizacion;
    private String origen;
    private Boolean tipoValor;
    private Long clasificacion;
    private Boolean iess;
    private Boolean ingresoAgravado;
    private String abreviatura;

    public Rubro() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Boolean porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getIngreso() {
        return ingreso;
    }

    public void setIngreso(Boolean ingreso) {
        this.ingreso = ingreso;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public Boolean getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(Boolean tipoValor) {
        this.tipoValor = tipoValor;
    }

    public Long getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Long clasificacion) {
        this.clasificacion = clasificacion;
    }

    public Boolean getIess() {
        return iess;
    }

    public void setIess(Boolean iess) {
        this.iess = iess;
    }

    public Boolean getIngresoAgravado() {
        return ingresoAgravado;
    }

    public void setIngresoAgravado(Boolean ingresoAgravado) {
        this.ingresoAgravado = ingresoAgravado;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }
}
