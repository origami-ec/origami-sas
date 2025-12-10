package org.sas.mail.model;

import org.sas.mail.entity.Usuario;

import java.io.File;
import java.util.List;

public class CorreoDTO {

    private Long referenciaId; //si se necesita actulizar si el correo se envio o no
    private String referencia;
    private String correoModulo; //por defecto coge notificacioneserp
    private String numeroTramite;
    private String destinatario;
    private String asunto;
    private String mensaje;
    private Usuario remitente;
    private Usuario destino;
    private List<CorreoArchivoDTO> archivos;
    private List<File> adjuntos;
    private Boolean enviado;
    private String texto;
    private String vinculo;
    private String textoVinculo;
    private String footerTexto;

    public CorreoDTO() {
    }

    public CorreoDTO(String destinatario, String asunto, String mensaje, List<CorreoArchivoDTO> archivos) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.archivos = archivos;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getVinculo() {
        return vinculo;
    }

    public void setVinculo(String vinculo) {
        this.vinculo = vinculo;
    }

    public String getTextoVinculo() {
        return textoVinculo;
    }

    public void setTextoVinculo(String textoVinculo) {
        this.textoVinculo = textoVinculo;
    }

    public String getFooterTexto() {
        return footerTexto;
    }

    public void setFooterTexto(String footerTexto) {
        this.footerTexto = footerTexto;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<CorreoArchivoDTO> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<CorreoArchivoDTO> archivos) {
        this.archivos = archivos;
    }

    public List<File> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<File> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public String getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(String numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public Usuario getRemitente() {
        return remitente;
    }

    public void setRemitente(Usuario remitente) {
        this.remitente = remitente;
    }

    public Usuario getDestino() {
        return destino;
    }

    public void setDestino(Usuario destino) {
        this.destino = destino;
    }

    public String getCorreoModulo() {
        return correoModulo;
    }

    public void setCorreoModulo(String correoModulo) {
        this.correoModulo = correoModulo;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }
}
