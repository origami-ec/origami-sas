package org.ibarra.util.model;

import java.io.File;
import java.util.List;

/**
 * The type Correo.
 */
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

    /**
     * Instantiates a new Correo.
     */
    public Correo() {
    }

    /**
     * Instantiates a new Correo.
     *
     * @param destinatario       the destinatario
     * @param nombresDetinatario the nombres detinatario
     * @param asunto             the asunto
     * @param texto              the texto
     * @param vinculo            the vinculo
     * @param textoVinculo       the texto vinculo
     * @param footerTexto        the footer texto
     */
    public Correo(String destinatario, String nombresDetinatario, String asunto, String texto, String vinculo, String textoVinculo, String footerTexto) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.texto = texto;
        this.vinculo = vinculo;
        this.textoVinculo = textoVinculo;
        this.footerTexto = footerTexto;
    }

    /**
     * Instantiates a new Correo.
     *
     * @param destinatario       the destinatario
     * @param asunto             the asunto
     * @param texto              the texto
     * @param archivos           the archivos
     * @param numeroTramite      the numero tramite
     * @param nombresDetinatario the nombres detinatario
     */
    public Correo(String destinatario, String asunto, String texto, List<CorreoArchivo> archivos, String numeroTramite, String nombresDetinatario) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.texto = texto;
        this.archivos = archivos;
    }

    /**
     * Gets mensaje.
     *
     * @return Es el texto completo del cuerpo del correo
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Sets mensaje.
     *
     * @param mensaje the mensaje
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Gets texto.
     *
     * @return Es el mensaje del correo (es lo que va a salir en body del correo)
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Sets texto.
     *
     * @param texto the texto
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Gets vinculo.
     *
     * @return La url que se envia en caso de existir un vinculo (OPCIONAL)
     */
    public String getVinculo() {
        return vinculo;
    }

    /**
     * Sets vinculo.
     *
     * @param vinculo the vinculo
     */
    public void setVinculo(String vinculo) {
        this.vinculo = vinculo;
    }

    /**
     * Gets texto vinculo.
     *
     * @return Es el texto que aparece sobre el vinculo por ejemplo Click Aquí (OPCIONAL)
     */
    public String getTextoVinculo() {
        return textoVinculo;
    }

    /**
     * Sets texto vinculo.
     *
     * @param textoVinculo the texto vinculo
     */
    public void setTextoVinculo(String textoVinculo) {
        this.textoVinculo = textoVinculo;
    }

    /**
     * Gets footer texto.
     *
     * @return Es el mensaje del pie de pagina (OPCIONAL)
     */
    public String getFooterTexto() {
        return footerTexto;
    }

    /**
     * Sets footer texto.
     *
     * @param footerTexto the footer texto
     */
    public void setFooterTexto(String footerTexto) {
        this.footerTexto = footerTexto;
    }

    /**
     * Gets destinatario.
     *
     * @return Correo a quien se envia el correo xD
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * Sets destinatario.
     *
     * @param destinatario the destinatario
     */
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * Gets asunto.
     *
     * @return the asunto
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Sets asunto.
     *
     * @param asunto the asunto
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * Gets archivos.
     *
     * @return the archivos
     */
    public List<CorreoArchivo> getArchivos() {
        return archivos;
    }

    /**
     * Sets archivos.
     *
     * @param archivos the archivos
     */
    public void setArchivos(List<CorreoArchivo> archivos) {
        this.archivos = archivos;
    }

    /**
     * Gets adjuntos.
     *
     * @return the adjuntos
     */
    public List<File> getAdjuntos() {
        return adjuntos;
    }

    /**
     * Sets adjuntos.
     *
     * @param adjuntos the adjuntos
     */
    public void setAdjuntos(List<File> adjuntos) {
        this.adjuntos = adjuntos;
    }

}
