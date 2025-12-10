package org.sas.administrativo.entity.configuracion;

import org.sas.administrativo.util.EsquemaConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "reporte", schema = EsquemaConfig.configuracion)
public class Reporte implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    private String descripcion;
    private String codigo;
    private String usuario;
    private Long codVersion;
    private Date fechaIngreso;
    private String usuarioIngreso;
    private Long modulo;
    private String reporte;
    private String estado;
    private String codigoSistema;
    private Long formato;
    private String archivo;
    private Long secuencia;

    public Reporte() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Long getCodVersion() {
        return codVersion;
    }

    public void setCodVersion(Long codVersion) {
        this.codVersion = codVersion;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Long getModulo() {
        return modulo;
    }

    public void setModulo(Long modulo) {
        this.modulo = modulo;
    }

    public String getReporte() {
        return reporte;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigoSistema() {
        return codigoSistema;
    }

    public void setCodigoSistema(String codigoSistema) {
        this.codigoSistema = codigoSistema;
    }

    public Long getFormato() {
        return formato;
    }

    public void setFormato(Long formato) {
        this.formato = formato;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Long getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Long secuencia) {
        this.secuencia = secuencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reporte reporte = (Reporte) o;
        return id.equals(reporte.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reporte{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", codigo='" + codigo + '\'' +
                ", usuario='" + usuario + '\'' +
                ", codVersion=" + codVersion +
                ", fechaIngreso=" + fechaIngreso +
                ", usuarioIngreso='" + usuarioIngreso + '\'' +
                ", modulo=" + modulo +
                ", reporte='" + reporte + '\'' +
                ", estado='" + estado + '\'' +
                ", codigoSistema='" + codigoSistema + '\'' +
                ", formato=" + formato +
                ", archivo='" + archivo + '\'' +
                ", secuencia=" + secuencia +
                '}';
    }
}
