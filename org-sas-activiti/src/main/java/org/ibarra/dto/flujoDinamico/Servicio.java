/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ibarra.dto.flujoDinamico;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.ibarra.entity.TipoTramite;
import org.ibarra.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ORIGAMI1
 */
public class Servicio implements Serializable {

    private Long id;
    private String abreviatura;
    private Integer diasRespuesta;
    private String estado;
    private Integer orden;
    private Integer hora;
    private Integer minutos;
    private String nombre;
    private Boolean online;
    private Long padreItem;
    private String imagen;
    private String pagina;
    private Boolean aplicaPredio;
    private Boolean aplicaTasa;
    private Departamento departamento;
    private Long departamentoId;
    private Long tipoTramite;
    private String usuarioRegistro;
    private String usuarioModifica;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaModifica;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaRegistro;
    private String tipo;
    private String descripcion;
    private Boolean revisionDeudas;
    private String url;

    private List<ServicioTarea> tareas;
    private TipoTramite tramite;

    public Servicio() {
    }

    public Servicio(Long id) {
        this.id = id;
    }

    public Servicio(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Servicio(Departamento departamento) {
        this.departamento = departamento;
        this.estado = "ACTIVO";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Integer getDiasRespuesta() {
        return diasRespuesta;
    }

    public void setDiasRespuesta(Integer diasRespuesta) {
        this.diasRespuesta = diasRespuesta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public Integer getMinutos() {
        return minutos;
    }

    public void setMinutos(Integer minutos) {
        this.minutos = minutos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Long getPadreItem() {
        return padreItem;
    }

    public void setPadreItem(Long padreItem) {
        this.padreItem = padreItem;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public Boolean getAplicaPredio() {
        return aplicaPredio;
    }

    public void setAplicaPredio(Boolean aplicaPredio) {
        this.aplicaPredio = aplicaPredio;
    }

    public Boolean getAplicaTasa() {
        return aplicaTasa;
    }

    public void setAplicaTasa(Boolean aplicaTasa) {
        this.aplicaTasa = aplicaTasa;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Long getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(Long tipoTramite) {
        this.tipoTramite = tipoTramite;
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

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getRevisionDeudas() {
        return revisionDeudas;
    }

    public void setRevisionDeudas(Boolean revisionDeudas) {
        this.revisionDeudas = revisionDeudas;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TipoTramite getTramite() {
        return tramite;
    }

    public void setTramite(TipoTramite tramite) {
        this.tramite = tramite;
    }

    public List<ServicioTarea> getTareas() {
        if (Utils.isEmpty(tareas)) {
            tareas = new ArrayList<>();
        }
        return tareas;
    }

    public void setTareas(List<ServicioTarea> tareas) {
        this.tareas = tareas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Servicio)) {
            return false;
        }
        Servicio other = (Servicio) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "id=" + id +
                ", abreviatura='" + abreviatura + '\'' +
                ", diasRespuesta=" + diasRespuesta +
                ", estado='" + estado + '\'' +
                ", orden=" + orden +
                ", hora=" + hora +
                ", minutos=" + minutos +
                ", nombre='" + nombre + '\'' +
                ", online=" + online +
                ", padreItem=" + padreItem +
                ", imagen='" + imagen + '\'' +
                ", pagina='" + pagina + '\'' +
                ", aplicaPredio=" + aplicaPredio +
                ", aplicaTasa=" + aplicaTasa +
                ", departamento=" + departamento +
                ", departamentoId=" + departamentoId +
                ", tipoTramite=" + tipoTramite +
                ", usuarioRegistro='" + usuarioRegistro + '\'' +
                ", usuarioModifica='" + usuarioModifica + '\'' +
                ", fechaModifica=" + fechaModifica +
                ", fechaRegistro=" + fechaRegistro +
                ", tipo='" + tipo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", revisionDeudas=" + revisionDeudas +
                ", url='" + url + '\'' +
                ", tareas=" + tareas +
                ", tramite=" + tramite +
                '}';
    }
}
