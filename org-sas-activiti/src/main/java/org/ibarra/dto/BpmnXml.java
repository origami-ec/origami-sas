package org.ibarra.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class BpmnXml implements Serializable {

    private TipoTramiteDto tipoTramite;
    private String xmlAnterior;
    private String xmlNuevo;
    private HashMap<String, Object> propsAnterior;
    private HashMap<String, Object> propsNuevo;
    private Boolean actualizarProceso;

    private Boolean activarNotificacion;

    private Integer tiempoActivacionPrevia;
    private Boolean repetirNotificacion;
    private Integer tiempoRepetirNotificacion;
    private List<ResumenTarea> resumenTareas;

    public TipoTramiteDto getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramiteDto tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public String getXmlAnterior() {
        return xmlAnterior;
    }

    public void setXmlAnterior(String xmlAnterior) {
        this.xmlAnterior = xmlAnterior;
    }

    public String getXmlNuevo() {
        return xmlNuevo;
    }

    public void setXmlNuevo(String xmlNuevo) {
        this.xmlNuevo = xmlNuevo;
    }

    public HashMap<String, Object> getPropsAnterior() {
        return propsAnterior;
    }

    public void setPropsAnterior(HashMap<String, Object> propsAnterior) {
        this.propsAnterior = propsAnterior;
    }

    public HashMap<String, Object> getPropsNuevo() {
        return propsNuevo;
    }

    public void setPropsNuevo(HashMap<String, Object> propsNuevo) {
        this.propsNuevo = propsNuevo;
    }

    public Boolean getActualizarProceso() {
        return actualizarProceso;
    }

    public void setActualizarProceso(Boolean actualizarProceso) {
        this.actualizarProceso = actualizarProceso;
    }

    public Boolean getActivarNotificacion() {
        return activarNotificacion;
    }

    public void setActivarNotificacion(Boolean activarNotificacion) {
        this.activarNotificacion = activarNotificacion;
    }

    public Integer getTiempoActivacionPrevia() {
        return tiempoActivacionPrevia;
    }

    public void setTiempoActivacionPrevia(Integer tiempoActivacionPrevia) {
        this.tiempoActivacionPrevia = tiempoActivacionPrevia;
    }

    public Boolean getRepetirNotificacion() {
        return repetirNotificacion;
    }

    public void setRepetirNotificacion(Boolean repetirNotificacion) {
        this.repetirNotificacion = repetirNotificacion;
    }

    public Integer getTiempoRepetirNotificacion() {
        return tiempoRepetirNotificacion;
    }

    public void setTiempoRepetirNotificacion(Integer tiempoRepetirNotificacion) {
        this.tiempoRepetirNotificacion = tiempoRepetirNotificacion;
    }

    public List<ResumenTarea> getResumenTareas() {
        return resumenTareas;
    }

    public void setResumenTareas(List<ResumenTarea> resumenTareas) {
        this.resumenTareas = resumenTareas;
    }

    @Override
    public String toString() {
        return "BpmnXml{" +
                "tipoTramite=" + tipoTramite +
                ", xmlAnterior='" + xmlAnterior + '\'' +
                ", xmlNuevo='" + xmlNuevo + '\'' +
                ", propsAnterior=" + propsAnterior +
                ", propsNuevo=" + propsNuevo +
                ", actualizarProceso=" + actualizarProceso +
                '}';
    }
}
