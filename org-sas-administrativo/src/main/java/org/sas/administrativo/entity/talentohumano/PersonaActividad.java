package org.sas.administrativo.entity.talentohumano;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "persona_actividad")
public class PersonaActividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "persona")
    private Long persona;
    @Column(name = "actividad_general")
    @Type(type = "org.hibernate.type.TextType")
    private String actividadGeneral;
    @Lob
    @Column(name = "identificacion")
    private String identificacion;
    @Lob
    @Column(name = "nombre")
    private String nombre;
    @Lob
    @Column(name = "cedula_contador")
    private String cedulaContador;
    @Lob
    @Column(name = "nombre_contador")
    private String nombreContador;
    @Lob
    @Column(name = "nombre_provincia")
    private String nombreProvincia;
    @Lob
    @Column(name = "nombre_regional")
    private String nombreRegional;
    @Lob
    @Column(name = "estado")
    private String estado;
    @Lob
    @Column(name = "cargo")
    private String cargo;

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersona() {
        return persona;
    }

    public void setPersona(Long persona) {
        this.persona = persona;
    }

    public String getActividadGeneral() {
        return actividadGeneral;
    }

    public void setActividadGeneral(String actividadGeneral) {
        this.actividadGeneral = actividadGeneral;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedulaContador() {
        return cedulaContador;
    }

    public void setCedulaContador(String cedulaContador) {
        this.cedulaContador = cedulaContador;
    }

    public String getNombreContador() {
        return nombreContador;
    }

    public void setNombreContador(String nombreContador) {
        this.nombreContador = nombreContador;
    }

    public String getNombreProvincia() {
        return nombreProvincia;
    }

    public void setNombreProvincia(String nombreProvincia) {
        this.nombreProvincia = nombreProvincia;
    }

    public String getNombreRegional() {
        return nombreRegional;
    }

    public void setNombreRegional(String nombreRegional) {
        this.nombreRegional = nombreRegional;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}