package org.ibarra.dto;

import java.io.Serializable;

public class RespuestaWs implements Serializable {

    private Long id;
    private Boolean estado;
    private String data;
    private String mensaje;


    public RespuestaWs() {

    }

    public RespuestaWs(Boolean estado, String data) {
        this.estado = estado;
        this.data = data;
    }

    public RespuestaWs(String data) {
        this.data = data;
    }

    public RespuestaWs(Boolean estado, String data, String mensaje) {
        this.data = data;
        this.estado = estado;
        this.mensaje = mensaje;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "Data{" +
                "data='" + data + '\'' +
                ", id=" + estado +
                ", unicode='" + mensaje + '\'' +
                '}';
    }
}
