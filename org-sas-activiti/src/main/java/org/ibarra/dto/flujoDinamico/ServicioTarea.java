/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ibarra.dto.flujoDinamico;

import org.ibarra.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author asanc
 */
public class ServicioTarea {

    private Long id;
    private Integer tiempo;
    private Integer orden;
    private String notas;
    private String pagina;
    private Boolean aprueba;
    private Boolean reasignar;
    private Boolean notifica;
    private Boolean validaFirma;
    private Boolean aplicaEmision;
    private Boolean finaliza;
    private String usuarioRegistro;
    private String usuarioModifica;

    private Departamento departamento;
    private Servicio servicio;
    private TareaVUE tarea;

    private List<ServicioTareaRelacion> sucesoras;
    private List<ServicioTareaRelacion> antecesoras;

    private List<ServicioResponsable> responsables;

    public ServicioTarea() {
    }

    public ServicioTarea(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTiempo() {
        return tiempo;
    }

    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public Boolean getAprueba() {
        return aprueba;
    }

    public void setAprueba(Boolean aprueba) {
        this.aprueba = aprueba;
    }

    public Boolean getReasignar() {
        return reasignar;
    }

    public void setReasignar(Boolean reasignar) {
        this.reasignar = reasignar;
    }

    public Boolean getValidaFirma() {
        return validaFirma;
    }

    public void setValidaFirma(Boolean validaFirma) {
        this.validaFirma = validaFirma;
    }

    public Boolean getAplicaEmision() {
        return aplicaEmision;
    }

    public void setAplicaEmision(Boolean aplicaEmision) {
        this.aplicaEmision = aplicaEmision;
    }

    public Boolean getNotifica() {
        return notifica;
    }

    public void setNotifica(Boolean notifica) {
        this.notifica = notifica;
    }

    public Boolean getFinaliza() {
        return finaliza;
    }

    public void setFinaliza(Boolean finaliza) {
        this.finaliza = finaliza;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public TareaVUE getTarea() {
        return tarea;
    }

    public void setTarea(TareaVUE tarea) {
        this.tarea = tarea;
    }

    public List<ServicioResponsable> getResponsables() {
        if (Utils.isEmpty(responsables)) {
            responsables = new ArrayList<>();
        }
        return responsables;
    }

    public void setResponsables(List<ServicioResponsable> responsables) {
        this.responsables = responsables;
    }

    public List<ServicioTareaRelacion> getSucesoras() {
        if (Utils.isEmpty(sucesoras)) {
            sucesoras = new ArrayList<>();
        }
        return sucesoras;
    }

    public void setSucesoras(List<ServicioTareaRelacion> sucesoras) {
        this.sucesoras = sucesoras;
    }

    public List<ServicioTareaRelacion> getAntecesoras() {
        if (Utils.isEmpty(antecesoras)) {
            antecesoras = new ArrayList<>();
        }
        return antecesoras;
    }

    public void setAntecesoras(List<ServicioTareaRelacion> antecesoras) {
        this.antecesoras = antecesoras;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ServicioTarea other = (ServicioTarea) obj;
        return Objects.equals(this.id, other.id);
    }

}
