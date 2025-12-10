package org.ibarra.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class SolicitudServicioDto {

    private Long id;
    private Long servicio;
    private Long servicioId;
    private Long solicitante;
    private Long predio;
    private String tramite;
    private String asignados;
    private String asunto;
    private String descripcion;
    private String informe;
    private Boolean asignado;
    private Boolean finalizado;
    private Integer referencia;
    private String tipo;
    private String estado;
    private String usuarioRegistro;
    private String usuarioModifica;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaRegistro;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaModifica;
    private Long elaborado;
    private Long revisado;
    private Long aprobado;
    private String documento;
    private String respuesta;
    private String comentario;
    private String caracteristica;
    private String inspeccion;
    private String codigoVerificacion;
    private String codigoVerificacionAprobado;
    private Integer operacion;
    private String certificadoRpi;

    public SolicitudServicioDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServicio() {
        return servicio;
    }

    public void setServicio(Long servicio) {
        this.servicio = servicio;
    }

    public Long getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Long solicitante) {
        this.solicitante = solicitante;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getAsignados() {
        return asignados;
    }

    public void setAsignados(String asignados) {
        this.asignados = asignados;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public Boolean getAsignado() {
        return asignado;
    }

    public void setAsignado(Boolean asignado) {
        this.asignado = asignado;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public Integer getReferencia() {
        return referencia;
    }

    public void setReferencia(Integer referencia) {
        this.referencia = referencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public Long getElaborado() {
        return elaborado;
    }

    public void setElaborado(Long elaborado) {
        this.elaborado = elaborado;
    }

    public Long getRevisado() {
        return revisado;
    }

    public void setRevisado(Long revisado) {
        this.revisado = revisado;
    }

    public Long getAprobado() {
        return aprobado;
    }

    public void setAprobado(Long aprobado) {
        this.aprobado = aprobado;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
    }

    public String getInspeccion() {
        return inspeccion;
    }

    public void setInspeccion(String inspeccion) {
        this.inspeccion = inspeccion;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public String getCodigoVerificacionAprobado() {
        return codigoVerificacionAprobado;
    }

    public void setCodigoVerificacionAprobado(String codigoVerificacionAprobado) {
        this.codigoVerificacionAprobado = codigoVerificacionAprobado;
    }

    public Long getPredio() {
        return predio;
    }

    public void setPredio(Long predio) {
        this.predio = predio;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getOperacion() {
        return operacion;
    }

    public void setOperacion(Integer operacion) {
        this.operacion = operacion;
    }

    public String getCertificadoRpi() {
        return certificadoRpi;
    }

    public void setCertificadoRpi(String certificadoRpi) {
        this.certificadoRpi = certificadoRpi;
    }
}
