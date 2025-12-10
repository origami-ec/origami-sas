package org.origami.docs.entity.origamigt.talentoHumano;

import java.math.BigDecimal;
import java.util.Date;

public class CargoRubro {
    private Long id;
    private Long cargo;
    private Rubro rubro;
    private Integer periodo;
    private String estado;
    private Integer proyeccion;
    private BigDecimal inicial;
    private Long cuenta;
    private Long itemPres;
    private Long fuente;
    private String partida;
    private Long tramite;
    private Long codigoReforma;
    private Long referencia;
    private Long estadoPre;
    private BigDecimal suplementaria;
    private BigDecimal codificado;
    private BigDecimal reduccion;
    private BigDecimal traspasoIncremento;
    private BigDecimal traspasoReduccion;
    private BigDecimal reserva;
    private BigDecimal comprometido;
    private Date fechaActualizacion;
    private String descripcion;
    private Boolean presupuestoAprobado;

    public CargoRubro() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCargo() {
        return cargo;
    }

    public void setCargo(Long cargo) {
        this.cargo = cargo;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getProyeccion() {
        return proyeccion;
    }

    public void setProyeccion(Integer proyeccion) {
        this.proyeccion = proyeccion;
    }

    public BigDecimal getInicial() {
        return inicial;
    }

    public void setInicial(BigDecimal inicial) {
        this.inicial = inicial;
    }

    public Long getCuenta() {
        return cuenta;
    }

    public void setCuenta(Long cuenta) {
        this.cuenta = cuenta;
    }

    public Long getItemPres() {
        return itemPres;
    }

    public void setItemPres(Long itemPres) {
        this.itemPres = itemPres;
    }

    public Long getFuente() {
        return fuente;
    }

    public void setFuente(Long fuente) {
        this.fuente = fuente;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public Long getCodigoReforma() {
        return codigoReforma;
    }

    public void setCodigoReforma(Long codigoReforma) {
        this.codigoReforma = codigoReforma;
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public Long getEstadoPre() {
        return estadoPre;
    }

    public void setEstadoPre(Long estadoPre) {
        this.estadoPre = estadoPre;
    }

    public BigDecimal getSuplementaria() {
        return suplementaria;
    }

    public void setSuplementaria(BigDecimal suplementaria) {
        this.suplementaria = suplementaria;
    }

    public BigDecimal getCodificado() {
        return codificado;
    }

    public void setCodificado(BigDecimal codificado) {
        this.codificado = codificado;
    }

    public BigDecimal getReduccion() {
        return reduccion;
    }

    public void setReduccion(BigDecimal reduccion) {
        this.reduccion = reduccion;
    }

    public BigDecimal getTraspasoIncremento() {
        return traspasoIncremento;
    }

    public void setTraspasoIncremento(BigDecimal traspasoIncremento) {
        this.traspasoIncremento = traspasoIncremento;
    }

    public BigDecimal getTraspasoReduccion() {
        return traspasoReduccion;
    }

    public void setTraspasoReduccion(BigDecimal traspasoReduccion) {
        this.traspasoReduccion = traspasoReduccion;
    }

    public BigDecimal getReserva() {
        return reserva;
    }

    public void setReserva(BigDecimal reserva) {
        this.reserva = reserva;
    }

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getPresupuestoAprobado() {
        return presupuestoAprobado;
    }

    public void setPresupuestoAprobado(Boolean presupuestoAprobado) {
        this.presupuestoAprobado = presupuestoAprobado;
    }
}
