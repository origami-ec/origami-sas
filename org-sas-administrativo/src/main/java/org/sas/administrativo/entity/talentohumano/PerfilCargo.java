package org.sas.administrativo.entity.talentohumano;


import org.hibernate.annotations.Formula;
import org.sas.administrativo.entity.configuracion.CatalogoItem;
import org.sas.administrativo.util.EsquemaConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "perfil_cargo", schema = EsquemaConfig.talentoHumano)
public class PerfilCargo implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String perfil;
    private String manual;
    private String estado;
    private String grado;
    private String nivel;
    private String mision;
    private String conocimiento;
    private String experiencia;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_cargo", referencedColumnName = "id")
    private CatalogoItem rolCargo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_ocupacional", referencedColumnName = "id")
    private CatalogoItem grupoOcupacional;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nivel_profesion", referencedColumnName = "id")
    private CatalogoItem nivelProfesion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instruccion_formal", referencedColumnName = "id")
    private CatalogoItem instruccionFormal;
    private Date fecha;
    private Date vigencia;
    private String organigramaCargo;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "especificidad_experiencia", referencedColumnName = "id")
    private CatalogoItem especificidadExperiencia;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_administrativa", referencedColumnName = "id")
    private UnidadAdministrativa unidadAdministrativa;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_conocimiento", referencedColumnName = "id")
    private CatalogoItem areaConocimiento;



    public PerfilCargo() {
    }

    public PerfilCargo(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public PerfilCargo(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getMision() {
        return mision;
    }

    public void setMision(String mision) {
        this.mision = mision;
    }

    public String getConocimiento() {
        return conocimiento;
    }

    public void setConocimiento(String conocimiento) {
        this.conocimiento = conocimiento;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public CatalogoItem getRolCargo() {
        return rolCargo;
    }

    public void setRolCargo(CatalogoItem rolCargo) {
        this.rolCargo = rolCargo;
    }

    public CatalogoItem getGrupoOcupacional() {
        return grupoOcupacional;
    }

    public void setGrupoOcupacional(CatalogoItem grupoOcupacional) {
        this.grupoOcupacional = grupoOcupacional;
    }

    public CatalogoItem getNivelProfesion() {
        return nivelProfesion;
    }

    public void setNivelProfesion(CatalogoItem nivelProfesion) {
        this.nivelProfesion = nivelProfesion;
    }

    public CatalogoItem getInstruccionFormal() {
        return instruccionFormal;
    }

    public void setInstruccionFormal(CatalogoItem instruccionFormal) {
        this.instruccionFormal = instruccionFormal;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getVigencia() {
        return vigencia;
    }

    public void setVigencia(Date vigencia) {
        this.vigencia = vigencia;
    }

    public String getOrganigramaCargo() {
        return organigramaCargo;
    }

    public void setOrganigramaCargo(String organigramaCargo) {
        this.organigramaCargo = organigramaCargo;
    }

    public CatalogoItem getEspecificidadExperiencia() {
        return especificidadExperiencia;
    }

    public void setEspecificidadExperiencia(CatalogoItem especificidadExperiencia) {
        this.especificidadExperiencia = especificidadExperiencia;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public CatalogoItem getAreaConocimiento() {
        return areaConocimiento;
    }

    public void setAreaConocimiento(CatalogoItem areaConocimiento) {
        this.areaConocimiento = areaConocimiento;
    }
}
