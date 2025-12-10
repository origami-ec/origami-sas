package org.sas.administrativo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Formula;
import org.sas.administrativo.entity.configuracion.CatalogoItem;
import org.sas.administrativo.entity.talentohumano.PersonaActividad;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "persona")
public class Persona implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numIdentificacion;
    private String nombre;
    private String apellido;
    private String domicilio;
    private String correo;
    private String telefono;
    private String celular;
    private Date fechaNacimiento;
    private Boolean estado;
    @JsonIgnore
    private Date fechaCreacion;
    private String estadoCivil;
    private String tituloProfesional;
    private String codigo;
    private String representanteLegal;
    private String condicionCiudadano;
    @JsonIgnore
    private Date fechaExpedicion;
    private String usuarioCreacion;
    private String observacion;
    private Boolean contactable;
    private String prefijoProfesion;
    @Formula("(select (case when length(num_identificacion) = 10 then concat(trim(nombre),' ',trim(apellido)) else (case when (nombre is null or trim(nombre) = '') then trim(apellido) else trim(nombre) end) end))")
    private String nombreCompleto;
    @Formula("(select date_part('year',age(fecha_nacimiento))::integer)")
    private Integer edad;
    @Transient
    private PersonaActividad actividad;


    public Persona() {
    }

    public Persona(Long id, String numIdentificacion, String nombre, String apellido, String domicilio, String correo, String telefono, String celular, Boolean estado, String estadoCivil, String condicionCiudadano,
                   String representanteLegal, Date fechaNacimiento) {
        this.id = id;
        this.numIdentificacion = numIdentificacion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.domicilio = domicilio;
        this.correo = correo;
        this.telefono = telefono;
        this.celular = celular;
        this.estado = estado;
        this.estadoCivil = estadoCivil;
        this.condicionCiudadano = condicionCiudadano;
        this.representanteLegal = representanteLegal;
        this.fechaNacimiento = fechaNacimiento;
    }


    public Persona(Long id) {
        this.id = id;
    }


    public Persona(String identificacion) {
        this.numIdentificacion = identificacion;
    }


    public Persona(Long id, String numIdentificacion) {
        if (id != null) {
            this.id = id;
        } else {
            this.numIdentificacion = numIdentificacion;
        }
        this.estado = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumIdentificacion() {
        return numIdentificacion;
    }

    public void setNumIdentificacion(String numIdentificacion) {
        this.numIdentificacion = numIdentificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }


    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getTituloProfesional() {
        return tituloProfesional;
    }

    public void setTituloProfesional(String tituloProfesional) {
        this.tituloProfesional = tituloProfesional;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public String getCondicionCiudadano() {
        return condicionCiudadano;
    }

    public void setCondicionCiudadano(String condicionCiudadano) {
        this.condicionCiudadano = condicionCiudadano;
    }

    public Date getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Date fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getContactable() {
        return contactable;
    }

    public void setContactable(Boolean contactable) {
        this.contactable = contactable;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public PersonaActividad getActividad() {
        return actividad;
    }

    public void setActividad(PersonaActividad actividad) {
        this.actividad = actividad;
    }

    public String getPrefijoProfesion() {
        return prefijoProfesion;
    }

    public void setPrefijoProfesion(String prefijoProfesion) {
        this.prefijoProfesion = prefijoProfesion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(id, persona.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", numIdentificacion='" + numIdentificacion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", celular='" + celular + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", estado=" + estado +
                ", fechaCreacion=" + fechaCreacion +
                ", estadoCivil='" + estadoCivil + '\'' +
                ", tituloProfesional='" + tituloProfesional + '\'' +
                ", codigo='" + codigo + '\'' +
                ", representanteLegal='" + representanteLegal + '\'' +
                ", condicionCiudadano='" + condicionCiudadano + '\'' +
                ", fechaExpedicion=" + fechaExpedicion +
                ", usuarioCreacion='" + usuarioCreacion + '\'' +
                ", observacion='" + observacion + '\'' +
                ", contactable=" + contactable +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", edad=" + edad +
                '}';
    }
}
