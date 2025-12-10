package org.sas.administrativo.dto.commons;

import org.sas.administrativo.dto.documental.UsuarioDocs;

import java.util.Date;

public class FirmaDocumentoDto {

    private Long id;
    private String tramite;
    private Long referencia;
    private Long servidor;
    private Integer movimientoX;
    private Integer movimientoY;
    private Integer posicionX;
    private Integer posicionY;
    private String usuario;
    private String motivo;
    private String palabraDesde;
    private String palabraHasta;
    private String documento;
    private String detalle;
    private Date fecha;
    private Date fechaFirma;
    private UsuarioDocs usuarioDocs;
    private CatalogoItemDto tipo;
    private CatalogoItemDto estado;
    private Integer numeroPagina;
    private Boolean manual;
    private String modulo;
    private String archivoFirmadoB64; //archivo en base64 desde la aplicacion de escritorio

    public FirmaDocumentoDto() {
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

    public UsuarioDocs getUsuarioDocs() {
        return usuarioDocs;
    }

    public void setUsuarioDocs(UsuarioDocs usuarioDocs) {
        this.usuarioDocs = usuarioDocs;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public CatalogoItemDto getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItemDto tipo) {
        this.tipo = tipo;
    }

    public CatalogoItemDto getEstado() {
        return estado;
    }

    public void setEstado(CatalogoItemDto estado) {
        this.estado = estado;
    }

    public String getArchivoFirmadoB64() {
        return archivoFirmadoB64;
    }

    public void setArchivoFirmadoB64(String archivoFirmadoB64) {
        this.archivoFirmadoB64 = archivoFirmadoB64;
    }

    public Integer getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(Integer numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    @Override
    public String toString() {
        return "FirmaDocumentoDto{" +
                "id=" + id +
                ", referencia=" + referencia +
                ", motivo='" + motivo + '\'' +
                '}';
    }
}
