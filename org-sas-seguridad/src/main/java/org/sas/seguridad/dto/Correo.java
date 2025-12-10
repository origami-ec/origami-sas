package org.sas.seguridad.dto;

import java.io.File;
import java.util.List;

public class Correo {

    private String destinatario;
    private String asunto;
    private String mensaje;
    private List<CorreoArchivo> archivos;
    private List<File> adjuntos;

    private String texto;
    private String vinculo;
    private String textoVinculo;
    private String footerTexto;

    public Correo() {
    }

    public Correo(String destinatario, String nombresDetinatario, String asunto, String texto, String vinculo, String textoVinculo, String footerTexto) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.texto = texto;
        this.vinculo = vinculo;
        this.textoVinculo = textoVinculo;
        this.footerTexto = footerTexto;
    }

    public Correo(String destinatario, String asunto, String texto, List<CorreoArchivo> archivos, String numeroTramite, String nombresDetinatario) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.texto = texto;
        this.archivos = archivos;
    }

    /**
     *
     * @return Es el texto completo del cuerpo del correo
     */
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     *
     * @return Es el mensaje del correo (es lo que va a salir en body del
     * correo)
     */
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     *
     * @return La url que se envia en caso de existir un vinculo (OPCIONAL)
     */
    public String getVinculo() {
        return vinculo;
    }

    public void setVinculo(String vinculo) {
        this.vinculo = vinculo;
    }

    /**
     * @return Es el texto que aparece sobre el vinculo por ejemplo Click Aquí
     * (OPCIONAL)
     */
    public String getTextoVinculo() {
        return textoVinculo;
    }

    public void setTextoVinculo(String textoVinculo) {
        this.textoVinculo = textoVinculo;
    }

    /**
     *
     * @return Es el mensaje del pie de pagina (OPCIONAL)
     */
    public String getFooterTexto() {
        return footerTexto;
    }

    public void setFooterTexto(String footerTexto) {
        this.footerTexto = footerTexto;
    }

    /**
     *
     * @return Correo a quien se envia el correo xD
     */
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

    public List<CorreoArchivo> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<CorreoArchivo> archivos) {
        this.archivos = archivos;
    }

    public List<File> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<File> adjuntos) {
        this.adjuntos = adjuntos;
    }

}
