package org.sas.administrativo.util.model;

import java.util.ArrayList;

public class DinardapFila {
    private ArrayList<BusquedaDinardapParametro> columnas;

    public DinardapFila() {
    }

    public ArrayList<BusquedaDinardapParametro> getColumnas() {
        return columnas;
    }

    public void setColumnas(ArrayList<BusquedaDinardapParametro> columnas) {
        this.columnas = columnas;
    }

    @Override
    public String toString() {
        return "DinardapFila{" +
                "columnas=" + columnas +
                '}';
    }
}
