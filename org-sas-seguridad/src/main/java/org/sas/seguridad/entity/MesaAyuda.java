package org.sas.seguridad.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(
        name = "mesa_ayuda"
)
public class MesaAyuda implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Basic(
            optional = false
    )
    @Column(
            name = "id"
    )
    private Long id;
    private Long solicitante;
    private Date fechaSolicita;
    private String modulo;
    private Long cargo;
    private String descripcion;
    private Long tecnico;
    private Date fechaAsignacion;
    private String observacion;
    private Date fechaModificacion;
    private String estado;
    private String prioridad;
    private Integer numeracion;
    private Integer periodo;
    private String codigo;
    private String usuarioSolicitante;
    private String tecnicoAsignado;
    private Date fechaEstimadaSolucion;
    private Date fechaCierre;
    private String respuestaTecnico;
    private String respuestaSolucion;
    private String encabezado;

    public MesaAyuda() {
    }

    public MesaAyuda(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaSolicita() {
        return this.fechaSolicita;
    }

    public void setFechaSolicita(Date fechaSolicita) {
        this.fechaSolicita = fechaSolicita;
    }

    public String getModulo() {
        return this.modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public Long getCargo() {
        return this.cargo;
    }

    public void setCargo(Long cargo) {
        this.cargo = cargo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getSolicitante() {
        return this.solicitante;
    }

    public void setSolicitante(Long solicitante) {
        this.solicitante = solicitante;
    }

    public Long getTecnico() {
        return this.tecnico;
    }

    public void setTecnico(Long tecnico) {
        this.tecnico = tecnico;
    }

    public String getUsuarioSolicitante() {
        return this.usuarioSolicitante;
    }

    public void setUsuarioSolicitante(String usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public String getTecnicoAsignado() {
        return this.tecnicoAsignado;
    }

    public void setTecnicoAsignado(String tecnicoAsignado) {
        this.tecnicoAsignado = tecnicoAsignado;
    }

    public Date getFechaAsignacion() {
        return this.fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaModificacion() {
        return this.fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrioridad() {
        return this.prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public Integer getNumeracion() {
        return this.numeracion;
    }

    public void setNumeracion(Integer numeracion) {
        this.numeracion = numeracion;
    }

    public Integer getPeriodo() {
        return this.periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFechaEstimadaSolucion() {
        return this.fechaEstimadaSolucion;
    }

    public void setFechaEstimadaSolucion(Date fechaEstimadaSolucion) {
        this.fechaEstimadaSolucion = fechaEstimadaSolucion;
    }

    public Date getFechaCierre() {
        return this.fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getRespuestaTecnico() {
        return this.respuestaTecnico;
    }

    public void setRespuestaTecnico(String respuestaTecnico) {
        this.respuestaTecnico = respuestaTecnico;
    }

    public String getRespuestaSolucion() {
        return this.respuestaSolucion;
    }

    public void setRespuestaSolucion(String respuestaSolucion) {
        this.respuestaSolucion = respuestaSolucion;
    }

    public String getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }

    public String toString() {
        return "MesaAyuda{id=" + this.id + ", solicitante=" + this.solicitante + ", fechaSolicita=" + this.fechaSolicita + ", modulo='" + this.modulo + "', cargo=" + this.cargo + ", descripcion='" + this.descripcion + "', tecnico=" + this.tecnico + ", fechaAsignacion=" + this.fechaAsignacion + ", observacion='" + this.observacion + "', fechaModificacion=" + this.fechaModificacion + ", estado='" + this.estado + "', prioridad='" + this.prioridad + "', numeracion=" + this.numeracion + ", periodo=" + this.periodo + ", codigo='" + this.codigo + "', usuarioSolicitante='" + this.usuarioSolicitante + "', tecnicoAsignado='" + this.tecnicoAsignado + "', fechaEstimadaSolucion=" + this.fechaEstimadaSolucion + ", fechaCierre=" + this.fechaCierre + ", respuestaTecnico='" + this.respuestaTecnico + "', respuestaSolucion='" + this.respuestaSolucion + "'}";
    }
}
