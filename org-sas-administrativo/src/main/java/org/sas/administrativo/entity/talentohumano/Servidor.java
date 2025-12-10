package org.sas.administrativo.entity.talentohumano;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Formula;
import org.sas.administrativo.entity.Persona;
import org.sas.administrativo.entity.configuracion.CatalogoItem;
import org.sas.administrativo.util.EsquemaConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "servidor", schema = EsquemaConfig.talentoHumano)
public class Servidor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date ingreso;
    private Date salida;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona", referencedColumnName = "id")
    private Persona persona;
    private String estado;
    private String emailInstitucion;
    private String telefonoOficina;
    private String actividad;
    private String extencion;
    private String nombreConyugue;
    private String cedulaConyugue;
    private String nombreEmergencia;
    private String apellidoEmergencia;
    private String telefonoEmergencia;
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
    @Column(name = "num_cargas_familiar")
    private String numCargasFamiliar;

    @Column(name = "telefono_institucional")
    private String telefonoInstitucional;

    @Column(name = "extension_telefonica")
    private String extensionTelefonica;


    private Boolean cargaFamiliar;
    private String documento;

    private String nombreEmergenciaSegundo;
    private String apellidoEmergenciaSegundo;
    private String telefonoEmergenciaSegundo;


    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaPermisoConsulta;
    @Transient
    private ServidorCargo servidorCargo;


    public Servidor() {
    }

    public Servidor(String estado) {
        this.estado = estado;
    }

    public Servidor(Long id) {
        this.id = id;
    }

    public Servidor(Persona persona) {
        this.persona = persona;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
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

    public String getNumCargasFamiliar() {
        return numCargasFamiliar;
    }

    public void setNumCargasFamiliar(String numCargasFamiliar) {
        this.numCargasFamiliar = numCargasFamiliar;
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

    public Boolean getCargaFamiliar() {
        return cargaFamiliar;
    }

    public void setCargaFamiliar(Boolean cargaFamiliar) {
        this.cargaFamiliar = cargaFamiliar;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombreEmergenciaSegundo() {
        return nombreEmergenciaSegundo;
    }

    public void setNombreEmergenciaSegundo(String nombreEmergenciaSegundo) {
        this.nombreEmergenciaSegundo = nombreEmergenciaSegundo;
    }

    public String getApellidoEmergenciaSegundo() {
        return apellidoEmergenciaSegundo;
    }

    public void setApellidoEmergenciaSegundo(String apellidoEmergenciaSegundo) {
        this.apellidoEmergenciaSegundo = apellidoEmergenciaSegundo;
    }

    public String getTelefonoEmergenciaSegundo() {
        return telefonoEmergenciaSegundo;
    }

    public void setTelefonoEmergenciaSegundo(String telefonoEmergenciaSegundo) {
        this.telefonoEmergenciaSegundo = telefonoEmergenciaSegundo;
    }

    public Date getFechaPermisoConsulta() {
        return fechaPermisoConsulta;
    }

    public void setFechaPermisoConsulta(Date fechaPermisoConsulta) {
        this.fechaPermisoConsulta = fechaPermisoConsulta;
    }



}
