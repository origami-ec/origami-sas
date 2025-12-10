package org.sas.administrativo.entity.talentohumano;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.annotations.Formula;
import org.sas.administrativo.entity.configuracion.CatalogoItem;
import org.sas.administrativo.util.EsquemaConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "servidor_cargo", schema = org.sas.administrativo.util.EsquemaConfig.talentoHumano)
public class ServidorCargo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    private Servidor servidor;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo", referencedColumnName = "id")
    private Cargo cargo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaAsignacion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaFinalizacion;
    private String estado;
    private String documento;
    @Transient
    private Boolean falta;
    private String observacion;
    @Transient
    private ServidorCargo servidorCargoAnt;
    @Transient
    private Integer periodo;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_contrato", referencedColumnName = "id")
    private CatalogoItem tipoContrato;

    public ServidorCargo() {
    }

    public ServidorCargo(Long id) {
        this.id = id;
    }

    public ServidorCargo(Servidor servidor) {
        this.servidor = servidor;
    }

    public ServidorCargo(Servidor servidor, Cargo cargo) {
        this.servidor = servidor;
        this.cargo = cargo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Boolean getFalta() {
        return falta;
    }

    public void setFalta(Boolean falta) {
        this.falta = falta;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public ServidorCargo getServidorCargoAnt() {
        return servidorCargoAnt;
    }

    public void setServidorCargoAnt(ServidorCargo servidorCargoAnt) {
        this.servidorCargoAnt = servidorCargoAnt;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public CatalogoItem getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(CatalogoItem tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

}
