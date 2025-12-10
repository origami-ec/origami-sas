package org.sas.administrativo.util.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BusquedaDinardap {

    @JsonProperty("Key")
    @SerializedName(value = "Key")
    private String key;
    private ArrayList<BusquedaDinardapParametro> parametros;

    public BusquedaDinardap() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<BusquedaDinardapParametro> getParametros() {
        return parametros;
    }

    public void setParametros(ArrayList<BusquedaDinardapParametro> parametros) {
        this.parametros = parametros;
    }

    @Override
    public String toString() {
        return "BusquedaDinardap{" +
                "key='" + key + '\'' +
                ", parametros=" + parametros +
                '}';
    }
}
