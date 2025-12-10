package org.sas.administrativo.util.model;

public class DinardapRespuesta {

    private String codMsg;
    private String descMsg;
    private DinardapInteroperadorPaquete interoperadorpaquete;

    public DinardapRespuesta() {
    }

    public String getCodMsg() {
        return codMsg;
    }

    public void setCodMsg(String codMsg) {
        this.codMsg = codMsg;
    }

    public String getDescMsg() {
        return descMsg;
    }

    public void setDescMsg(String descMsg) {
        this.descMsg = descMsg;
    }

    public DinardapInteroperadorPaquete getInteroperadorpaquete() {
        return interoperadorpaquete;
    }

    public void setInteroperadorpaquete(DinardapInteroperadorPaquete interoperadorpaquete) {
        this.interoperadorpaquete = interoperadorpaquete;
    }

    @Override
    public String toString() {
        return "DinardapRespuesta{" +
                "codMsg='" + codMsg + '\'' +
                ", descMsg='" + descMsg + '\'' +
                ", interoperadorpaquete=" + interoperadorpaquete +
                '}';
    }
}
