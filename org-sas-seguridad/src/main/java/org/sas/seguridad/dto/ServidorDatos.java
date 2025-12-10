package org.sas.seguridad.dto;

import java.io.Serializable;

public class ServidorDatos implements Serializable  {
    private Long id;
    private Long personaID;
    private String identificacion;
    private String servidor;
    private String titulo;
    private String cargo;
    private String nombres;
    private String apellidos;
    private Boolean sexo;
    private Long unidadAdministrativaID;
    private String unidadAdministrativa;
    private String correoPersonal;
    private String correoInstitucional;
    private String cargoOriginal;
    private Long unidadAdministrativaOriginalID;
    private String unidadAdministrativaOriginal;
    private String emailInstitucion;
    private String telefonoInstitucional;
    private String extensionTelefonica;
    private String fechaCumpleanio;
    private Boolean cumpleanio;
    private UnidadAdministrativaDto direccionAdministrativa;


    public ServidorDatos() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Long getUnidadAdministrativaID() {
        return unidadAdministrativaID;
    }

    public void setUnidadAdministrativaID(Long unidadAdministrativaID) {
        this.unidadAdministrativaID = unidadAdministrativaID;
    }

    public String getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(String unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public Long getPersonaID() {
        return personaID;
    }

    public void setPersonaID(Long personaID) {
        this.personaID = personaID;
    }

    public String getCorreoPersonal() {
        return correoPersonal;
    }

    public void setCorreoPersonal(String correoPersonal) {
        this.correoPersonal = correoPersonal;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public String getCargoOriginal() {
        return cargoOriginal;
    }

    public void setCargoOriginal(String cargoOriginal) {
        this.cargoOriginal = cargoOriginal;
    }

    public Long getUnidadAdministrativaOriginalID() {
        return unidadAdministrativaOriginalID;
    }

    public void setUnidadAdministrativaOriginalID(Long unidadAdministrativaOriginalID) {
        this.unidadAdministrativaOriginalID = unidadAdministrativaOriginalID;
    }

    public String getUnidadAdministrativaOriginal() {
        return unidadAdministrativaOriginal;
    }

    public void setUnidadAdministrativaOriginal(String unidadAdministrativaOriginal) {
        this.unidadAdministrativaOriginal = unidadAdministrativaOriginal;
    }

    public String getEmailInstitucion() {
        return emailInstitucion;
    }

    public void setEmailInstitucion(String emailInstitucion) {
        this.emailInstitucion = emailInstitucion;
    }

    public String getTelefonoInstitucional() {
        return telefonoInstitucional;
    }

    public void setTelefonoInstitucional(String telefonoInstitucional) {
        this.telefonoInstitucional = telefonoInstitucional;
    }

    public String getExtensionTelefonica() {
        return extensionTelefonica;
    }

    public void setExtensionTelefonica(String extensionTelefonica) {
        this.extensionTelefonica = extensionTelefonica;
    }

    public String getFechaCumpleanio() {
        return fechaCumpleanio;
    }

    public void setFechaCumpleanio(String fechaCumpleanio) {
        this.fechaCumpleanio = fechaCumpleanio;
    }

    public Boolean getCumpleanio() {
        return cumpleanio;
    }

    public void setCumpleanio(Boolean cumpleanio) {
        this.cumpleanio = cumpleanio;
    }

    public Boolean getSexo() {
        return sexo;
    }

    public void setSexo(Boolean sexo) {
        this.sexo = sexo;
    }

    public UnidadAdministrativaDto getDireccionAdministrativa() {
        return direccionAdministrativa;
    }

    public void setDireccionAdministrativa(UnidadAdministrativaDto direccionAdministrativa) {
        this.direccionAdministrativa = direccionAdministrativa;
    }

    @Override
    public String toString() {
        return "ServidorDatos{" +
                "id=" + id +
                ", servidor='" + servidor + '\'' +
                ", titulo='" + titulo + '\'' +
                ", cargo='" + cargo + '\'' +
                '}';
    }
}
