package org.sas.zull.service;
import org.sas.zull.dto.ActiveDirectoryModel;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final String mensaje;
    private final String response;
    private final Boolean estado;
    private ActiveDirectoryModel activeDirectoryResponse;


    public JwtResponse(String jwttoken, String mensaje,Boolean estado, String response) {
        this.jwttoken = jwttoken;
        this.mensaje = mensaje;
        this.estado = estado;
        this.response = response;
    }
    public JwtResponse(String jwttoken, String mensaje,Boolean estado, String response, ActiveDirectoryModel activeDirectoryResponse) {
        this.jwttoken = jwttoken;
        this.mensaje = mensaje;
        this.estado = estado;
        this.response = response;
        this.activeDirectoryResponse = activeDirectoryResponse;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public Boolean getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getResponse() {
        return response;
    }

    public ActiveDirectoryModel getActiveDirectoryResponse() {
        return activeDirectoryResponse;
    }
}

