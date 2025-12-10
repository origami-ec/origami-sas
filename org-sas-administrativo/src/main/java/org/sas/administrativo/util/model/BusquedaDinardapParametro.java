package org.sas.administrativo.util.model;

public class BusquedaDinardapParametro {
    private String nombre;
    private String campo;
    private String valor;

    public BusquedaDinardapParametro() {
    }

    public BusquedaDinardapParametro(String nombre, String valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "BusquedaDinardapParametro{" +
                "nombre='" + nombre + '\'' +
                ", campo='" + campo + '\'' +
                ", valor='" + valor + '\'' +
                '}';
    }
}
