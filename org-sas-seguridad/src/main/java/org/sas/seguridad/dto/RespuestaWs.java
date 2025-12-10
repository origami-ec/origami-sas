package org.sas.seguridad.dto;

import java.io.Serializable;

/**
 * The type Respuesta ws.
 */
public class RespuestaWs implements Serializable {

    private Long id;
    private Boolean estado;
    private String data;
    private String mensaje;
    private String xError;


    /**
     * Instantiates a new Respuesta ws.
     */
    public RespuestaWs() {

    }

    public RespuestaWs(Long id) {
        this.id = id;
    }

    public RespuestaWs(String data) {
        this.data = data;
    }
    /**
     * Instantiates a new Respuesta ws.
     *
     * @param estado the estado
     * @param data   the data
     */
    public RespuestaWs(Boolean estado, String data) {
        this.estado = estado;
        this.data = data;
    }

    /**
     * Instantiates a new Respuesta ws.
     *
     * @param estado  the estado
     * @param data    the data
     * @param mensaje the mensaje
     */
    public RespuestaWs(Boolean estado, String data, String mensaje) {
        this.data = data;
        this.estado = estado;
        this.mensaje = mensaje;
    }

    public RespuestaWs(Long id, Boolean estado, String data, String mensaje) {
        this.id = id;
        this.estado = estado;
        this.data = data;
        this.mensaje = mensaje;
    }

    /**
     * Gets estado.
     *
     * @return the estado
     */
    public Boolean getEstado() {
        return estado;
    }

    /**
     * Sets estado.
     *
     * @param estado the estado
     */
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Gets mensaje.
     *
     * @return the mensaje
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getxError() {
        return xError;
    }

    public void setxError(String xError) {
        this.xError = xError;
    }

    @Override
    public String toString() {
        return "RespuestaWs{" +
                "id=" + id +
                ", estado=" + estado +
                ", data='" + data + '\'' +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }
}
