package org.origami.docs.entity;

import java.util.List;

public class IndexacionCampo {
    private String tipoDato;
    private String descripcion;
    private Boolean obligatorio;
    private Boolean obligatorioVentanillaPublica;
    private Boolean movil;
    private List<String> categorias; //para cuando el tipo de dato sea categorico

    public IndexacionCampo() {
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public Boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(Boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public Boolean getMovil() {
        return movil;
    }

    public void setMovil(Boolean movil) {
        this.movil = movil;
    }

    public Boolean getObligatorioVentanillaPublica() {
        return obligatorioVentanillaPublica;
    }

    public void setObligatorioVentanillaPublica(Boolean obligatorioVentanillaPublica) {
        this.obligatorioVentanillaPublica = obligatorioVentanillaPublica;
    }
}
