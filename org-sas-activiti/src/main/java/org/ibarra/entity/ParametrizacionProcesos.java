package org.ibarra.entity;

import org.ibarra.util.EsquemaConfig;

import jakarta.persistence.*;

@Entity
@Table(name = "parametrizacion_procesos", schema = EsquemaConfig.procesos)
public class ParametrizacionProcesos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    private String idTarea;
    private String rol;
    private String abreviaturaProceso;
    private Long tipoTramite;
    private Boolean diriguidoRol;
    private String varUsuario;
    private String varForm;
    private String descripcionProceso;
    private String urlForm;
    private String descripcionTarea;
    private String condiciones;
    private String persona;
    private Boolean tieneCondicion;
    private Boolean validarFirma;

    public ParametrizacionProcesos() {

    }

    public ParametrizacionProcesos(String idTarea, String abreviaturaProceso) {
        this.idTarea = idTarea;
        this.abreviaturaProceso = abreviaturaProceso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(String idTarea) {
        this.idTarea = idTarea;
    }


    public String getAbreviaturaProceso() {
        return abreviaturaProceso;
    }

    public void setAbreviaturaProceso(String abreviaturaProceso) {
        this.abreviaturaProceso = abreviaturaProceso;
    }

    public Long getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(Long tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Boolean getDiriguidoRol() {
        return diriguidoRol;
    }

    public void setDiriguidoRol(Boolean diriguidoRol) {
        this.diriguidoRol = diriguidoRol;
    }

    public String getVarUsuario() {
        return varUsuario;
    }

    public void setVarUsuario(String varUsuario) {
        this.varUsuario = varUsuario;
    }

    public String getVarForm() {
        return varForm;
    }

    public void setVarForm(String varForm) {
        this.varForm = varForm;
    }

    public String getDescripcionProceso() {
        return descripcionProceso;
    }

    public void setDescripcionProceso(String descripcionProceso) {
        this.descripcionProceso = descripcionProceso;
    }

    public String getUrlForm() {
        return urlForm;
    }

    public void setUrlForm(String urlForm) {
        this.urlForm = urlForm;
    }

    public String getDescripcionTarea() {
        return descripcionTarea;
    }

    public void setDescripcionTarea(String descripcionTarea) {
        this.descripcionTarea = descripcionTarea;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public Boolean getTieneCondicion() {
        return tieneCondicion;
    }

    public void setTieneCondicion(Boolean tieneCondicion) {
        this.tieneCondicion = tieneCondicion;
    }

    public Boolean getValidarFirma() {
        return validarFirma;
    }

    public void setValidarFirma(Boolean validarFirma) {
        this.validarFirma = validarFirma;
    }

    @Override
    public String toString() {
        return "ParametrizacionProcesos{" +
                "id=" + id +
                ", idTarea='" + idTarea + '\'' +
                ", rol='" + rol + '\'' +
                ", abreviaturaProceso='" + abreviaturaProceso + '\'' +
                ", tipoTramite=" + tipoTramite +
                ", diriguidoRol=" + diriguidoRol +
                ", varUsuario='" + varUsuario + '\'' +
                ", varForm='" + varForm + '\'' +
                ", descripcionProceso='" + descripcionProceso + '\'' +
                ", urlForm='" + urlForm + '\'' +
                ", descripcionTarea='" + descripcionTarea + '\'' +
                ", condiciones='" + condiciones + '\'' +
                ", persona='" + persona + '\'' +
                ", tieneCondicion=" + tieneCondicion +
                '}';
    }
}