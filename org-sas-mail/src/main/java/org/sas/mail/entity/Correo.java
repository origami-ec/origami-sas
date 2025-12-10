package org.sas.mail.entity;

import org.bson.codecs.pojo.annotations.BsonId;
import org.sas.mail.utils.EmailUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("correo")
public class Correo implements Serializable {

    @BsonId
    private String id;
    private String destinatario;
    private String numeroTramite;
    private String asunto;
    private String mensaje;
    private Boolean enviado;
    private Boolean reenviado;
    private Date fechaEnvio;
    private Usuario remitente;
    private Usuario destino;
    private String correoModulo; //por defecto coge notificacioneserp
    private List<CorreoArchivo> archivos;

    private Long referenciaId; //si se necesita actulizar si el correo se envio o no
    private String referencia;
    private String texto;
    private String vinculo;
    private String textoVinculo;
    private String footerTexto;

    public Correo() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(String numeroTramite) {
        this.numeroTramite = numeroTramite;
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

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
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

    public List<CorreoArchivo> getArchivos() {
        if(EmailUtils.isEmpty(archivos)){
            archivos = new ArrayList<>();
        }
        return archivos;
    }

    public void setArchivos(List<CorreoArchivo> archivos) {
        this.archivos = archivos;
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

    public Boolean getReenviado() {
        return reenviado;
    }

    public void setReenviado(Boolean reenviado) {
        this.reenviado = reenviado;
    }

    @Override
    public String toString() {
        return "Correo{" +
                "id='" + id + '\'' +
                ", destinatario='" + destinatario + '\'' +
                ", numeroTramite='" + numeroTramite + '\'' +
                ", asunto='" + asunto + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", enviado=" + enviado +
                ", fechaEnvio=" + fechaEnvio +
                ", remitente=" + remitente +
                ", destino=" + destino +
                ", archivos=" + archivos +
                '}';
    }
}
