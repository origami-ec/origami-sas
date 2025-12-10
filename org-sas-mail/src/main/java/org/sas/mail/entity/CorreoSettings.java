package org.sas.mail.entity;

import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("correoSettings")
public class CorreoSettings {
    @BsonId
    private String id;
    private String correo;
    private String razonSocial;
    private String correoClave;
    private String correoHost;
    private String correoPort;
    private String codigo;
    private String correoModulo; //por defecto coge notificacioneserp

    public CorreoSettings() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreoClave() {
        return correoClave;
    }

    public void setCorreoClave(String correoClave) {
        this.correoClave = correoClave;
    }

    public String getCorreoHost() {
        return correoHost;
    }

    public void setCorreoHost(String correoHost) {
        this.correoHost = correoHost;
    }

    public String getCorreoPort() {
        return correoPort;
    }

    public void setCorreoPort(String correoPort) {
        this.correoPort = correoPort;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCorreoModulo() {
        return correoModulo;
    }

    public void setCorreoModulo(String correoModulo) {
        this.correoModulo = correoModulo;
    }

    @Override
    public String toString() {
        return "CorreoSettings{" +
                "id='" + id + '\'' +
                ", correo='" + correo + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", correoClave='" + correoClave + '\'' +
                ", correoHost='" + correoHost + '\'' +
                ", correoPort='" + correoPort + '\'' +
                ", codigo='" + codigo + '\'' +
                ", correoModulo='" + correoModulo + '\'' +
                '}';
    }
}
