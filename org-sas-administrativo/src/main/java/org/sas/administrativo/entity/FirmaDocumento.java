package org.sas.administrativo.entity;


import org.sas.administrativo.entity.configuracion.CatalogoItem;
import org.sas.administrativo.util.ValoresCodigo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "firma_documento")
public class FirmaDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tramite;
    private Long referencia;
    private Long servidor;
    @Column(name = "movimiento_x")
    private Integer movimientoX;
    @Column(name = "movimiento_y")
    private Integer movimientoY;
    @Column(name = "posicion_x")
    private Integer posicionX;
    @Column(name = "posicion_y")
    private Integer posicionY;
    private String usuario;
    private String motivo;
    private String palabraDesde;
    private String palabraHasta;
    private String documento;
    private String modulo;
    private String detalle;
    private Date fecha;
    private Date fechaFirma;
    private Integer numeroPagina;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private CatalogoItem tipo;
    @JoinColumn(name = "estado", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private CatalogoItem estado;
    private Boolean manual;

    public FirmaDocumento() {
        modulo = ValoresCodigo.administrativo;
        this.manual = Boolean.FALSE;
    }
    public FirmaDocumento(Long referencia, Long servidor, CatalogoItem tipo, CatalogoItem estado) {
        this.referencia = referencia;
        this.servidor = servidor;
        this.tipo = tipo;
        this.estado = estado;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public Long getServidor() {
        return servidor;
    }

    public void setServidor(Long servidor) {
        this.servidor = servidor;
    }

    public Integer getMovimientoX() {
        return movimientoX;
    }

    public void setMovimientoX(Integer movimientoX) {
        this.movimientoX = movimientoX;
    }

    public Integer getMovimientoY() {
        return movimientoY;
    }

    public void setMovimientoY(Integer movimientoY) {
        this.movimientoY = movimientoY;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getPalabraDesde() {
        return palabraDesde;
    }

    public void setPalabraDesde(String palabraDesde) {
        this.palabraDesde = palabraDesde;
    }

    public String getPalabraHasta() {
        return palabraHasta;
    }

    public void setPalabraHasta(String palabraHasta) {
        this.palabraHasta = palabraHasta;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(Date fechaFirma) {
        this.fechaFirma = fechaFirma;
    }

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
    }

    public CatalogoItem getEstado() {
        return estado;
    }

    public void setEstado(CatalogoItem estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Integer getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(Integer posicionX) {
        this.posicionX = posicionX;
    }

    public Integer getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(Integer posicionY) {
        this.posicionY = posicionY;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

    public Integer getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(Integer numeroPagina) {
        this.numeroPagina = numeroPagina;
    }
}
