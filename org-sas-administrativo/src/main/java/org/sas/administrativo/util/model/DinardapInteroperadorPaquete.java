package org.sas.administrativo.util.model;

import java.util.ArrayList;

public class DinardapInteroperadorPaquete {

    private ArrayList<DinardapEntidad> entidades;
    private String numeroPaquete;

    public DinardapInteroperadorPaquete() {
    }

    public ArrayList<DinardapEntidad> getEntidades() {
        return entidades;
    }

    public void setEntidades(ArrayList<DinardapEntidad> entidades) {
        this.entidades = entidades;
    }

    public String getNumeroPaquete() {
        return numeroPaquete;
    }

    public void setNumeroPaquete(String numeroPaquete) {
        this.numeroPaquete = numeroPaquete;
    }
}
