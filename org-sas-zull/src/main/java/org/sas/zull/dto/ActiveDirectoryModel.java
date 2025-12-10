package org.sas.zull.dto;

public class ActiveDirectoryModel {

    private Boolean estado;
    private String response;

    public ActiveDirectoryModel() {
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
