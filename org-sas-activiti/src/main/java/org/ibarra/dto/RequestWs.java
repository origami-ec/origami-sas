package org.ibarra.dto;

import java.util.Date;
import java.util.List;

public class RequestWs {

    private Long id;
    private Integer tipo;
    private String data;
    private String mensaje;
    private String referencia;
    private String ip;
    private Boolean estado;
    private List result;
    private String email;
    private Date fechaRespuesta;


    public RequestWs() {
    }

    public RequestWs(Long id) {
        this.id = id;
    }

    public RequestWs(String data) {
        this.data = data;
    }

    public RequestWs(Long id, String data) {
        this.id = id;
        this.data = data;
    }

    public RequestWs(Long id, String mensaje, String data, boolean estado) {
        this.id = id;
        this.mensaje = mensaje;
        this.data = data;
        this.estado = estado;
    }

    public RequestWs(Long id, String data, String referencia) {
        this.id = id;
        this.referencia = referencia;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(Date fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }


    @Override
    public String toString() {
        return "RequestWs{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", data='" + data + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", referencia='" + referencia + '\'' +
                ", ip='" + ip + '\'' +
                ", estado=" + estado +
                ", result=" + result +
                ", email='" + email + '\'' +
                '}';
    }
}
