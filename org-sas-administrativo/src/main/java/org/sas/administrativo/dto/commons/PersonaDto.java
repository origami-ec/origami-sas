package org.sas.administrativo.dto.commons;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public class PersonaDto {

    private Long id;
    private String numIdentificacion;
    private String nombre;
    private String apellido;
    private String domicilio;
    private String correo;
    private String telefono;
    private String celular;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;
    private Boolean discapacidad;
    private String numConadis;
    private Boolean estado;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaCreacion;
    private String estadoCivil;
    private Long tipoIdentificacion;
    private Integer porcentajeDiscapacidad;
    private Boolean sexo;
    private String condicionCiudadano;
    private String representanteLegal;
    private Boolean naturalezaJuridica;
    private String direccionPrestServ;
    private String nombreCompleto;

    private Boolean contactable;
    private String telefonoContacto;
    private String celularContacto;
    private String correoContacto;
    private String orientacionSexual;
    private String tituloProfesional;



    public PersonaDto() {
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

    public Boolean getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(Boolean discapacidad) {
        this.discapacidad = discapacidad;
    }

    public String getNumConadis() {
        return numConadis;
    }

    public void setNumConadis(String numConadis) {
        this.numConadis = numConadis;
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

    public Long getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(Long tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Integer getPorcentajeDiscapacidad() {
        return porcentajeDiscapacidad;
    }

    public void setPorcentajeDiscapacidad(Integer porcentajeDiscapacidad) {
        this.porcentajeDiscapacidad = porcentajeDiscapacidad;
    }

    public Boolean getSexo() {
        return sexo;
    }

    public void setSexo(Boolean sexo) {
        this.sexo = sexo;
    }

    public String getCondicionCiudadano() {
        return condicionCiudadano;
    }

    public void setCondicionCiudadano(String condicionCiudadano) {
        this.condicionCiudadano = condicionCiudadano;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public Boolean getNaturalezaJuridica() {
        return naturalezaJuridica;
    }

    public void setNaturalezaJuridica(Boolean naturalezaJuridica) {
        this.naturalezaJuridica = naturalezaJuridica;
    }

    public String getDireccionPrestServ() {
        return direccionPrestServ;
    }

    public void setDireccionPrestServ(String direccionPrestServ) {
        this.direccionPrestServ = direccionPrestServ;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getCelularContacto() {
        return celularContacto;
    }

    public void setCelularContacto(String celularContacto) {
        this.celularContacto = celularContacto;
    }

    public String getCorreoContacto() {
        return correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }

    public Boolean getContactable() {
        return contactable;
    }

    public void setContactable(Boolean contactable) {
        this.contactable = contactable;
    }


    public String getOrientacionSexual() {
        return orientacionSexual;
    }

    public void setOrientacionSexual(String orientacionSexual) {
        this.orientacionSexual = orientacionSexual;
    }

    public String getTituloProfesional() {
        return tituloProfesional;
    }

    public void setTituloProfesional(String tituloProfesional) {
        this.tituloProfesional = tituloProfesional;
    }

    @Override
    public String toString() {
        return "PersonaDto{" +
                "id=" + id +
                ", numIdentificacion='" + numIdentificacion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", celular='" + celular + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", discapacidad=" + discapacidad +
                ", numConadis='" + numConadis + '\'' +
                ", estado=" + estado +
                ", fechaCreacion=" + fechaCreacion +
                ", estadoCivil='" + estadoCivil + '\'' +
                ", tipoIdentificacion=" + tipoIdentificacion +
                ", porcentajeDiscapacidad=" + porcentajeDiscapacidad +
                ", sexo=" + sexo +
                ", condicionCiudadano='" + condicionCiudadano + '\'' +
                ", representanteLegal='" + representanteLegal + '\'' +
                ", naturalezaJuridica=" + naturalezaJuridica +
                ", direccionPrestServ='" + direccionPrestServ + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", contactable=" + contactable +
                ", telefonoContacto='" + telefonoContacto + '\'' +
                ", celularContacto='" + celularContacto + '\'' +
                ", correoContacto='" + correoContacto + '\'' +
                ", orientacionSexual='" + orientacionSexual + '\'' +
                '}';
    }
}
