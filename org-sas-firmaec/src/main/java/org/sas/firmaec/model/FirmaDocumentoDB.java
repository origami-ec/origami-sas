package org.sas.firmaec.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FirmaDocumentoDB {
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
    private String tipoDesk;
    private String palabraDesde;
    private String palabraHasta;
    private String documento;
    private String modulo;
    private String detalle;
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private String fecha;
    private Date fechaFirma;


    //CAMPOS PARA LA FIRMA DESDE ESCRITORIO
    private String clave;
    private String estadoFirmado;
    private String archivo;
    private String archivoFirmadoB64; //archivo en base64 desde la aplicacion de escritorio
    private String token; //ES EL ALIAS Y EN EL CASO DE P12 SERA EL ARCHIVO

    public FirmaDocumentoDB() {
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Date getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(Date fechaFirma) {
        this.fechaFirma = fechaFirma;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEstadoFirmado() {
        return estadoFirmado;
    }

    public void setEstadoFirmado(String estadoFirmado) {
        this.estadoFirmado = estadoFirmado;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getArchivoFirmadoB64() {
        return archivoFirmadoB64;
    }

    public void setArchivoFirmadoB64(String archivoFirmadoB64) {
        this.archivoFirmadoB64 = archivoFirmadoB64;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getTipoDesk() {
        return tipoDesk;
    }

    public void setTipoDesk(String tipoDesk) {
        this.tipoDesk = tipoDesk;
    }

    @Override
    public String toString() {
        return "FirmaDocumentoDB{" +
                "id=" + id +
                ", tramite='" + tramite + '\'' +
                ", referencia=" + referencia +
                ", servidor=" + servidor +
                ", movimientoX=" + movimientoX +
                ", movimientoY=" + movimientoY +
                ", posicionX=" + posicionX +
                ", posicionY=" + posicionY +
                ", usuario='" + usuario + '\'' +
                ", motivo='" + motivo + '\'' +
                ", tipo='" + tipoDesk + '\'' +
                ", palabraDesde='" + palabraDesde + '\'' +
                ", palabraHasta='" + palabraHasta + '\'' +
                ", documento='" + documento + '\'' +
                ", modulo='" + modulo + '\'' +
                ", detalle='" + detalle + '\'' +
                ", fecha='" + fecha + '\'' +
                ", fechaFirma=" + fechaFirma +
                ", clave='" + clave + '\'' +
                ", estadoFirmado='" + estadoFirmado + '\'' +
                ", archivo='" + archivo + '\'' +
                ", archivoFirmadoB64='" + archivoFirmadoB64 + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
