package org.ibarra.entity;

import jakarta.persistence.*;
import org.ibarra.util.EsquemaConfig;

import java.util.Date;

@Entity
@Table(name = "historico_tramite", schema = EsquemaConfig.procesos, indexes = {@Index(name = "idx_historico_tramite_proceso", columnList = "idProceso"), @Index(name = "idx_historico_tramite_tramite", columnList = "tramite")})
public class HistoricoTramite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tramite;

    private String idProceso;
    private String idProcesoTemp;
    private Long referenciaId;
    private String referencia; //Se puede guardar cualquier ID o informacion pertinente
    private Integer periodo;
    private String concepto;
    private String usuarioCreacion;
    private Boolean estado;
    private Boolean documento;
    private Boolean entregado;
    private Date fecha;
    private Date fechaIngreso;
    private Date fechaEntrega;
    private Date fechaRetiro;
    private Date fechaDesblock;
    private Date fechaNotificacion;
    @JoinColumn(name = "tipo_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoTramite tipoTramite;
    @JoinColumn(name = "solicitante", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Persona solicitante;

    public HistoricoTramite() {
    }

    public HistoricoTramite(Long id) {
        this.id = id;
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

    public Persona getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Persona solicitante) {
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

    public Boolean getEntregado() {
        return entregado;
    }

    public void setEntregado(Boolean entregado) {
        this.entregado = entregado;
    }

    public Date getFechaDesblock() {
        return fechaDesblock;
    }

    public void setFechaDesblock(Date fechaDesblock) {
        this.fechaDesblock = fechaDesblock;
    }

    public Boolean getDocumento() {
        return documento;
    }

    public void setDocumento(Boolean documento) {
        this.documento = documento;
    }

    public Date getFechaNotificacion() {
        return fechaNotificacion;
    }

    public void setFechaNotificacion(Date fechaNotificacion) {
        this.fechaNotificacion = fechaNotificacion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }


    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    @Override
    public String toString() {
        return "HistoricoTramite{" + "id=" + id + ", tramite='" + tramite + '\'' + ", solicitante=" + solicitante + ", idProceso='" + idProceso + '\'' + ", idProcesoTemp='" + idProcesoTemp + '\'' + ", referenciaId=" + referenciaId + ", referencia='" + referencia + '\'' + ", periodo=" + periodo + ", concepto='" + concepto + '\'' + ", usuarioCreacion='" + usuarioCreacion + '\'' + ", estado=" + estado + ", documento=" + documento + ", entregado=" + entregado + ", fecha=" + fecha + ", fechaIngreso=" + fechaIngreso + ", fechaEntrega=" + fechaEntrega + ", fechaRetiro=" + fechaRetiro + ", fechaDesblock=" + fechaDesblock + ", fechaNotificacion=" + fechaNotificacion + ", tipoTramite=" + tipoTramite + '}';
    }
}
