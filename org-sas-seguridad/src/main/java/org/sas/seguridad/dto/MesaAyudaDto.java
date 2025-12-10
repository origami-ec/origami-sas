package org.sas.seguridad.dto;

import java.util.Date;
import java.util.List;

public class MesaAyudaDto {
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
    private List<DocumentoTramiteDto> listaDocumentos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Long solicitante) {
        this.solicitante = solicitante;
    }

    public Date getFechaSolicita() {
        return fechaSolicita;
    }

    public void setFechaSolicita(Date fechaSolicita) {
        this.fechaSolicita = fechaSolicita;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public Long getCargo() {
        return cargo;
    }

    public void setCargo(Long cargo) {
        this.cargo = cargo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getTecnico() {
        return tecnico;
    }

    public void setTecnico(Long tecnico) {
        this.tecnico = tecnico;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public Integer getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(Integer numeracion) {
        this.numeracion = numeracion;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUsuarioSolicitante() {
        return usuarioSolicitante;
    }

    public void setUsuarioSolicitante(String usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public String getTecnicoAsignado() {
        return tecnicoAsignado;
    }

    public void setTecnicoAsignado(String tecnicoAsignado) {
        this.tecnicoAsignado = tecnicoAsignado;
    }

    public Date getFechaEstimadaSolucion() {
        return fechaEstimadaSolucion;
    }

    public void setFechaEstimadaSolucion(Date fechaEstimadaSolucion) {
        this.fechaEstimadaSolucion = fechaEstimadaSolucion;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getRespuestaTecnico() {
        return respuestaTecnico;
    }

    public void setRespuestaTecnico(String respuestaTecnico) {
        this.respuestaTecnico = respuestaTecnico;
    }

    public String getRespuestaSolucion() {
        return respuestaSolucion;
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

    public List<DocumentoTramiteDto> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<DocumentoTramiteDto> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }
}
