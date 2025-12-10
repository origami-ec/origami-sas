package org.sas.gateway.utils;

public class RespuestaWs {
    private String mensaje;
    private String codigoError;
    private Boolean estadoError;

    public RespuestaWs() {
    }

    public RespuestaWs(String mensaje, String codigoError, Boolean estadoError) {
        this.mensaje = mensaje;
        this.codigoError = codigoError;
        this.estadoError = estadoError;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    public Boolean getEstadoError() {
        return estadoError;
    }

    public void setEstadoError(Boolean estadoError) {
        this.estadoError = estadoError;
    }

    @Override
    public String toString() {
        return "RespuestaWs{" +
                "mensaje='" + mensaje + '\'' +
                ", codigoError='" + codigoError + '\'' +
                ", estadoError=" + estadoError +
                '}';
    }
}
