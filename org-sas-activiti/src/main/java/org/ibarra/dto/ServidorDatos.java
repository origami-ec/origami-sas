package org.ibarra.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.ibarra.dto.flujoDinamico.ServidorCargoDto;
import org.ibarra.dto.flujoDinamico.UnidadAdministrativaDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ServidorDatos implements Serializable {

    private Long id;
    private Long personaID;
    private String servidor;
    private String identificacion;
    private String nombres;
    private String apellidos;
    private String titulo;
    private String cargo;
    private Long cargoID;
    private Long unidadAdministrativaID;
    private String unidadAdministrativa;
    private String correoPersonal;
    private String correoInstitucional;
    private String serEstadoCivil;
    private String fechaNacimiento;
    private String grupoSanguineo;
    private String numeroTelefono;
    private String direccion;
    private String genero;
    private String estado;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date ingreso;

    private String cargoOriginal;

    private Long unidadAdministrativaOriginalID;

    private String unidadAdministrativaOriginal;

    private String emailInstitucion;
    private String telefonoInstitucional;
    private String extensionTelefonica;
    private String fechaCumpleanio;
    private Boolean cumpleanio;

    private Long escalaSalarialID;
    private Integer anioLaborado;
    private Integer mesLaborado;
    private BigDecimal derechoVacacion;

    //para biometricos
    private String programa;
    private String centroCosto;
    private String codBiometrico;
    private ServidorCargoDto servidorCargos;
    private UnidadAdministrativaDto direccionAdministrativa;

    public ServidorDatos() {
    }

    public ServidorDatos(Long id, Long personaID, String identificacion, String servidor, String nombres, String apellidos, String titulo, String cargo, Long unidadAdministrativaID, String unidadAdministrativa) {
        this.id = id;
        this.personaID = personaID;
        this.identificacion = identificacion;
        this.servidor = servidor;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.titulo = titulo;
        this.cargo = cargo;
        this.unidadAdministrativaID = unidadAdministrativaID;
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSerEstadoCivil() {
        return serEstadoCivil;
    }

    public void setSerEstadoCivil(String serEstadoCivil) {
        this.serEstadoCivil = serEstadoCivil;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public Long getCargoID() {
        return cargoID;
    }

    public void setCargoID(Long cargoID) {
        this.cargoID = cargoID;
    }

    public Long getEscalaSalarialID() {
        return escalaSalarialID;
    }

    public void setEscalaSalarialID(Long escalaSalarialID) {
        this.escalaSalarialID = escalaSalarialID;
    }

    public Integer getAnioLaborado() {
        return anioLaborado;
    }

    public void setAnioLaborado(Integer anioLaborado) {
        this.anioLaborado = anioLaborado;
    }

    public Integer getMesLaborado() {
        return mesLaborado;
    }

    public void setMesLaborado(Integer mesLaborado) {
        this.mesLaborado = mesLaborado;
    }

    public BigDecimal getDerechoVacacion() {
        return derechoVacacion;
    }

    public void setDerechoVacacion(BigDecimal derechoVacacion) {
        this.derechoVacacion = derechoVacacion;
    }

    public Date getIngreso() {
        return ingreso;
    }

    public void setIngreso(Date ingreso) {
        this.ingreso = ingreso;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(String centroCosto) {
        this.centroCosto = centroCosto;
    }

    public String getCodBiometrico() {
        return codBiometrico;
    }

    public void setCodBiometrico(String codBiometrico) {
        this.codBiometrico = codBiometrico;
    }

    public ServidorCargoDto getServidorCargos() {
        return servidorCargos;
    }

    public void setServidorCargos(ServidorCargoDto servidorCargos) {
        this.servidorCargos = servidorCargos;
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
                ", personaID=" + personaID +
                ", servidor='" + servidor + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", titulo='" + titulo + '\'' +
                ", cargo='" + cargo + '\'' +
                ", unidadAdministrativaID=" + unidadAdministrativaID +
                ", unidadAdministrativa='" + unidadAdministrativa + '\'' +
                ", correoPersonal='" + correoPersonal + '\'' +
                ", correoInstitucional='" + correoInstitucional + '\'' +
                ", serEstadoCivil='" + serEstadoCivil + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", grupoSanguineo='" + grupoSanguineo + '\'' +
                ", numeroTelefono='" + numeroTelefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", genero='" + genero + '\'' +
                ", estado='" + estado + '\'' +
                ", cargoOriginal='" + cargoOriginal + '\'' +
                ", unidadAdministrativaOriginalID=" + unidadAdministrativaOriginalID +
                ", unidadAdministrativaOriginal='" + unidadAdministrativaOriginal + '\'' +
                ", emailInstitucion='" + emailInstitucion + '\'' +
                ", telefonoInstitucional='" + telefonoInstitucional + '\'' +
                ", extensionTelefonica='" + extensionTelefonica + '\'' +
                '}';
    }
}
