package org.origami.docs.model;

import org.origami.docs.entity.UnidadAdministrativa;
import org.origami.docs.entity.origamigt.calidad.ListadoMaestroDetalle;
import org.origami.docs.entity.origamigt.calidad.MapaProceso;
import org.origami.docs.entity.origamigt.calidad.MapaProcesoDetalle;

import java.util.List;

public class ArchivoIndexDto {

    private Long referenciaId;
    private String referencia;
    private String tramite;
    private String numTramite;
    private String tipoIndexacion;
    private String detalleDocumento;//CAMPO PARA AGREGAR AL ARCHIVO como criterio de busqueda
    private Boolean estado;
    private String formatoUpload;
    private String textoDocumento;
    private MapaProcesoDetalle subproceso;
    private MapaProceso proceso;
    private ListadoMaestroDetalle listadoMaestro;
    private List<ArchivoIndexCampoDto> detalles;

    public ArchivoIndexDto() {
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

    public String getTipoIndexacion() {
        return tipoIndexacion;
    }

    public void setTipoIndexacion(String tipoIndexacion) {
        this.tipoIndexacion = tipoIndexacion;
    }

    public String getDetalleDocumento() {
        return detalleDocumento;
    }

    public void setDetalleDocumento(String detalleDocumento) {
        this.detalleDocumento = detalleDocumento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List<ArchivoIndexCampoDto> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<ArchivoIndexCampoDto> detalles) {
        this.detalles = detalles;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getTextoDocumento() {
        return textoDocumento;
    }

    public void setTextoDocumento(String textoDocumento) {
        this.textoDocumento = textoDocumento;
    }

    public String getFormatoUpload() {
        return formatoUpload;
    }

    public void setFormatoUpload(String formatoUpload) {
        this.formatoUpload = formatoUpload;
    }

    public MapaProcesoDetalle getSubproceso() {
        return subproceso;
    }

    public void setSubproceso(MapaProcesoDetalle subproceso) {
        this.subproceso = subproceso;
    }

    public MapaProceso getProceso() {
        return proceso;
    }

    public void setProceso(MapaProceso proceso) {
        this.proceso = proceso;
    }

    public ListadoMaestroDetalle getListadoMaestro() {
        return listadoMaestro;
    }

    public void setListadoMaestro(ListadoMaestroDetalle listadoMaestro) {
        this.listadoMaestro = listadoMaestro;
    }

}
