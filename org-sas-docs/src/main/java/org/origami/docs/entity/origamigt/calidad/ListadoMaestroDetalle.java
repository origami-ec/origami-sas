package org.origami.docs.entity.origamigt.calidad;

import org.origami.docs.entity.UnidadAdministrativa;

import java.util.Date;
import java.util.Objects;

public class ListadoMaestroDetalle {

    private Long id;
    private String descripcion;
    private String codigo;
    private String usuario;
    private String codVersion;
    private String usuarioIngreso;
    private Long modulo;
    private String reporte;
    private String estado;
    private String codigoSistema;
    private Long formato;
    private String archivo;
    private Long secuencia;
    private String rutaArchivo;

    private UnidadAdministrativa unidadAdministrativa;
    private String formatoTipo;
    private String responsable;
    private String medioAlmacenamiento;
    private String recuperacion;
    private String retencion;

    public ListadoMaestroDetalle() {
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

    public String getCodVersion() {
        return codVersion;
    }

    public void setCodVersion(String codVersion) {
        this.codVersion = codVersion;
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

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getFormatoTipo() {
        return formatoTipo;
    }

    public void setFormatoTipo(String formatoTipo) {
        this.formatoTipo = formatoTipo;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getMedioAlmacenamiento() {
        return medioAlmacenamiento;
    }

    public void setMedioAlmacenamiento(String medioAlmacenamiento) {
        this.medioAlmacenamiento = medioAlmacenamiento;
    }

    public String getRecuperacion() {
        return recuperacion;
    }

    public void setRecuperacion(String recuperacion) {
        this.recuperacion = recuperacion;
    }

    public String getRetencion() {
        return retencion;
    }

    public void setRetencion(String retencion) {
        this.retencion = retencion;
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ListadoMaestroDetalle other = (ListadoMaestroDetalle) obj;
        return Objects.equals(this.id, other.id);
    }

}
