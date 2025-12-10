package org.origami.docs.entity.origamigt.talentoHumano;

import org.origami.docs.entity.origamigt.configuracion.CatalogoItem;
import org.origami.docs.entity.origamigt.configuracion.Persona;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "servidor")
public class Servidor {
    @Id
    public String _id;
    private Long idServidor;
    private Date ingreso;
    private Date salida;
    private Persona persona;
    private String estado;
    private CatalogoItem estadoCivil;
    private CatalogoItem etnia;
    private String emailInstitucion;
    private String telefonoOficina;
    private String actividad;
    private String extencion;
    private String nombreConyugue;
    private String cedulaConyugue;
    private CatalogoItem tipoSangre;
    private String nombreEmergencia;
    private String apellidoEmergencia;
    private String telefonoEmergencia;
    private CatalogoItem motivoSalida;
    private String actaSalida;
    private Date fechaActa;
    private String codBiometrico;
    private Boolean realizaHoraExtra;
    private Integer maximoHoraExtra;
    private String puntoMarcacion;
    private Boolean jubilado;
    private Integer diaAcumuladoVacacion;
    private String descripcionSalida;
    private Boolean decimoTercero;
    private Boolean decimoCuarto;
    private Boolean fondosReserva;
    private Boolean derechoFondosReserva;
    private Boolean enfermedad;
    private String rutaFirmaDigital;
    private ServidorCargo servidorCargo;

    public Servidor() {
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Long getIdServidor() {
        return idServidor;
    }

    public void setIdServidor(Long idServidor) {
        this.idServidor = idServidor;
    }

    public Date getIngreso() {
        return ingreso;
    }

    public void setIngreso(Date ingreso) {
        this.ingreso = ingreso;
    }

    public Date getSalida() {
        return salida;
    }

    public void setSalida(Date salida) {
        this.salida = salida;
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

    public String getTelefonoOficina() {
        return telefonoOficina;
    }

    public void setTelefonoOficina(String telefonoOficina) {
        this.telefonoOficina = telefonoOficina;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getExtencion() {
        return extencion;
    }

    public void setExtencion(String extencion) {
        this.extencion = extencion;
    }

    public String getNombreConyugue() {
        return nombreConyugue;
    }

    public void setNombreConyugue(String nombreConyugue) {
        this.nombreConyugue = nombreConyugue;
    }

    public String getCedulaConyugue() {
        return cedulaConyugue;
    }

    public void setCedulaConyugue(String cedulaConyugue) {
        this.cedulaConyugue = cedulaConyugue;
    }


    public String getNombreEmergencia() {
        return nombreEmergencia;
    }

    public void setNombreEmergencia(String nombreEmergencia) {
        this.nombreEmergencia = nombreEmergencia;
    }

    public String getApellidoEmergencia() {
        return apellidoEmergencia;
    }

    public void setApellidoEmergencia(String apellidoEmergencia) {
        this.apellidoEmergencia = apellidoEmergencia;
    }

    public String getTelefonoEmergencia() {
        return telefonoEmergencia;
    }

    public void setTelefonoEmergencia(String telefonoEmergencia) {
        this.telefonoEmergencia = telefonoEmergencia;
    }


    public String getActaSalida() {
        return actaSalida;
    }

    public void setActaSalida(String actaSalida) {
        this.actaSalida = actaSalida;
    }

    public Date getFechaActa() {
        return fechaActa;
    }

    public void setFechaActa(Date fechaActa) {
        this.fechaActa = fechaActa;
    }

    public String getCodBiometrico() {
        return codBiometrico;
    }

    public void setCodBiometrico(String codBiometrico) {
        this.codBiometrico = codBiometrico;
    }

    public Boolean getRealizaHoraExtra() {
        return realizaHoraExtra;
    }

    public void setRealizaHoraExtra(Boolean realizaHoraExtra) {
        this.realizaHoraExtra = realizaHoraExtra;
    }

    public Integer getMaximoHoraExtra() {
        return maximoHoraExtra;
    }

    public void setMaximoHoraExtra(Integer maximoHoraExtra) {
        this.maximoHoraExtra = maximoHoraExtra;
    }

    public String getPuntoMarcacion() {
        return puntoMarcacion;
    }

    public void setPuntoMarcacion(String puntoMarcacion) {
        this.puntoMarcacion = puntoMarcacion;
    }

    public Boolean getJubilado() {
        return jubilado;
    }

    public void setJubilado(Boolean jubilado) {
        this.jubilado = jubilado;
    }

    public Integer getDiaAcumuladoVacacion() {
        return diaAcumuladoVacacion;
    }

    public void setDiaAcumuladoVacacion(Integer diaAcumuladoVacacion) {
        this.diaAcumuladoVacacion = diaAcumuladoVacacion;
    }

    public String getDescripcionSalida() {
        return descripcionSalida;
    }

    public void setDescripcionSalida(String descripcionSalida) {
        this.descripcionSalida = descripcionSalida;
    }

    public Boolean getDecimoTercero() {
        return decimoTercero;
    }

    public void setDecimoTercero(Boolean decimoTercero) {
        this.decimoTercero = decimoTercero;
    }

    public Boolean getDecimoCuarto() {
        return decimoCuarto;
    }

    public void setDecimoCuarto(Boolean decimoCuarto) {
        this.decimoCuarto = decimoCuarto;
    }

    public Boolean getFondosReserva() {
        return fondosReserva;
    }

    public void setFondosReserva(Boolean fondosReserva) {
        this.fondosReserva = fondosReserva;
    }

    public Boolean getDerechoFondosReserva() {
        return derechoFondosReserva;
    }

    public void setDerechoFondosReserva(Boolean derechoFondosReserva) {
        this.derechoFondosReserva = derechoFondosReserva;
    }

    public Boolean getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(Boolean enfermedad) {
        this.enfermedad = enfermedad;
    }

    public String getRutaFirmaDigital() {
        return rutaFirmaDigital;
    }

    public void setRutaFirmaDigital(String rutaFirmaDigital) {
        this.rutaFirmaDigital = rutaFirmaDigital;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public CatalogoItem getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(CatalogoItem estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public CatalogoItem getEtnia() {
        return etnia;
    }

    public void setEtnia(CatalogoItem etnia) {
        this.etnia = etnia;
    }

    public CatalogoItem getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(CatalogoItem tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public CatalogoItem getMotivoSalida() {
        return motivoSalida;
    }

    public void setMotivoSalida(CatalogoItem motivoSalida) {
        this.motivoSalida = motivoSalida;
    }

    public ServidorCargo getServidorCargo() {
        return servidorCargo;
    }

    public void setServidorCargo(ServidorCargo servidorCargo) {
        this.servidorCargo = servidorCargo;
    }
}
