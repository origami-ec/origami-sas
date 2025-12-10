package org.ibarra.dto;

public class PersonaDto {

    private Long id;
    private String numIdentificacion;
    private String nombre;
    private String apellido;
    private String domicilio;
    private String correo;
    private String telefono;
    private String celular;
    private Boolean discapacidad;
    private String numConadis;
    private Boolean estado;
    private String estadoCivil;
    private Long tipoIdentificacion;
    private Integer porcentajeDiscapacidad;
    private Boolean sexo;
    private String condicionCiudadano;
    private String representanteLegal;
    private Boolean naturalezaJuridica;
    private String direccionPrestServ;
    private String nombreCompleto;
    private String correoInstitucional;
    private String correoVentanilla;

    public PersonaDto() {
    }

    public PersonaDto(Long id, String numIdentificacion, String nombre, String apellido, String domicilio, String correo, String telefono, String celular, Boolean discapacidad, String numConadis, Boolean estado, String estadoCivil, Long tipoIdentificacion, Integer porcentajeDiscapacidad, Boolean sexo, String condicionCiudadano, String representanteLegal, Boolean naturalezaJuridica, String direccionPrestServ, String nombreCompleto) {
        this.id = id;
        this.numIdentificacion = numIdentificacion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.domicilio = domicilio;
        this.correo = correo;
        this.telefono = telefono;
        this.celular = celular;
        this.discapacidad = discapacidad;
        this.numConadis = numConadis;
        this.estado = estado;
        this.estadoCivil = estadoCivil;
        this.tipoIdentificacion = tipoIdentificacion;
        this.porcentajeDiscapacidad = porcentajeDiscapacidad;
        this.sexo = sexo;
        this.condicionCiudadano = condicionCiudadano;
        this.representanteLegal = representanteLegal;
        this.naturalezaJuridica = naturalezaJuridica;
        this.direccionPrestServ = direccionPrestServ;
        this.nombreCompleto = nombreCompleto;
    }

    public PersonaDto(Long id) {
        this.id = id;
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

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public String getCorreoVentanilla() {
        return correoVentanilla;
    }

    public void setCorreoVentanilla(String correoVentanilla) {
        this.correoVentanilla = correoVentanilla;
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
                ", discapacidad=" + discapacidad +
                ", numConadis='" + numConadis + '\'' +
                ", estado=" + estado +
                ", estadoCivil='" + estadoCivil + '\'' +
                ", tipoIdentificacion=" + tipoIdentificacion +
                ", porcentajeDiscapacidad=" + porcentajeDiscapacidad +
                ", sexo=" + sexo +
                ", condicionCiudadano='" + condicionCiudadano + '\'' +
                ", representanteLegal='" + representanteLegal + '\'' +
                ", naturalezaJuridica=" + naturalezaJuridica +
                ", direccionPrestServ='" + direccionPrestServ + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", correoInstitucional='" + correoInstitucional + '\'' +
                ", correoVentanilla='" + correoVentanilla + '\'' +
                '}';
    }
}
