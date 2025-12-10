package org.sas.administrativo.util.model;

import java.util.ArrayList;

public class DinardapEntidad {

    private ArrayList<DinardapFila> filas;
    private String nombre;

    public DinardapEntidad() {
    }

    public ArrayList<DinardapFila> getFilas() {
        return filas;
    }

    public void setFilas(ArrayList<DinardapFila> filas) {
        this.filas = filas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "DinardapEntidad{" +
                "filas=" + filas +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
