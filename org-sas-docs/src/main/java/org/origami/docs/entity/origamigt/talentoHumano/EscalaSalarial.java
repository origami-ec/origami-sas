package org.origami.docs.entity.origamigt.talentoHumano;

import java.math.BigDecimal;

public class EscalaSalarial {
    private Long id;
    private String grupoOrganizacional;
    private Long grado;
    private BigDecimal remuneracionDolares;

    public EscalaSalarial() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrupoOrganizacional() {
        return grupoOrganizacional;
    }

    public void setGrupoOrganizacional(String grupoOrganizacional) {
        this.grupoOrganizacional = grupoOrganizacional;
    }

    public Long getGrado() {
        return grado;
    }

    public void setGrado(Long grado) {
        this.grado = grado;
    }

    public BigDecimal getRemuneracionDolares() {
        return remuneracionDolares;
    }

    public void setRemuneracionDolares(BigDecimal remuneracionDolares) {
        this.remuneracionDolares = remuneracionDolares;
    }
}
