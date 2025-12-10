package org.sas.seguridad.dto;


import java.util.Date;
import java.util.List;

public class HistoricoTramiteDto {

    private Long id;
    private String  tramite;
    private PersonaDto solicitante;
    private String idProceso;
    private String idProcesoTemp;
    private Long referenciaId;
    private String referencia; //Se puede guardar cualquier ID o informacion pertinente
    private Integer periodo;
    private String concepto;
    private Boolean estado;
    private Boolean documento;
    private Boolean entregado;
    private Date fecha;
    private Date fechaIngreso;
    private Date fechaEntrega;
    private Date fechaRetiro;
    private Date fechaDesblock;
    private Date fechaNotificacion;
    private TipoTramiteDto tipoTramite;
    private List<DocumentoTramiteDto> documentos;

    public HistoricoTramiteDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public PersonaDto getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(PersonaDto solicitante) {
        this.solicitante = solicitante;
    }

    public String getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(String idProceso) {
        this.idProceso = idProceso;
    }

    public String getIdProcesoTemp() {
        return idProcesoTemp;
    }

    public void setIdProcesoTemp(String idProcesoTemp) {
        this.idProcesoTemp = idProcesoTemp;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getDocumento() {
        return documento;
    }

    public void setDocumento(Boolean documento) {
        this.documento = documento;
    }

    public Boolean getEntregado() {
        return entregado;
    }

    public void setEntregado(Boolean entregado) {
        this.entregado = entregado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Date getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(Date fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public Date getFechaDesblock() {
        return fechaDesblock;
    }

    public void setFechaDesblock(Date fechaDesblock) {
        this.fechaDesblock = fechaDesblock;
    }

    public Date getFechaNotificacion() {
        return fechaNotificacion;
    }

    public void setFechaNotificacion(Date fechaNotificacion) {
        this.fechaNotificacion = fechaNotificacion;
    }

    public TipoTramiteDto getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramiteDto tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public List<DocumentoTramiteDto> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<DocumentoTramiteDto> documentos) {
        this.documentos = documentos;
    }
}
