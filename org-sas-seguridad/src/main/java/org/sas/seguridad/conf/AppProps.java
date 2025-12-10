package org.sas.seguridad.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProps {
    @Value("${app.urlApp}")
    private String urlApp;
    @Value("${app.urlZuul}")
    private String urlZuul;
    @Value("${app.administrativo}")
    private String administrativo;
    private String codigoSha;
    private Integer codigoTiempo;
    private Integer codigoLongitud;

    private String activarUsuarioAsunto;
    private String activarUsuarioMensaje;
    private String activarUsuarioVinculo;

    private String actualizarUsuarioAsunto;
    private String actualizarUsuarioMensaje;
    private String actualizarUsuarioVinculo;

    private String actualizarDobleVerificacionAsunto;
    private String actualizarDobleVerificacionMensaje;

    private String usuarioActivo;
    private String usuarioInactivo;
    private String usuarioPendiente;
    private String usuarioBloqueado;

    /*   private List<Menu> menus;
       private Map<String, List<MenuTipo>> menuXusuario;
   */
    public AppProps() {
    }

    public String getUrlApp() {
        return urlApp;
    }

    public String getUrlZuul() {
        return urlZuul;
    }

    public String getAdministrativo() {
        return administrativo;
    }

    public String getCodigoSha() {
        return codigoSha;
    }

    public void setCodigoSha(String codigoSha) {
        this.codigoSha = codigoSha;
    }

    public Integer getCodigoTiempo() {
        return codigoTiempo;
    }

    public void setCodigoTiempo(Integer codigoTiempo) {
        this.codigoTiempo = codigoTiempo;
    }

    public Integer getCodigoLongitud() {
        return codigoLongitud;
    }

    public void setCodigoLongitud(Integer codigoLongitud) {
        this.codigoLongitud = codigoLongitud;
    }

    public String getActivarUsuarioAsunto() {
        return activarUsuarioAsunto;
    }

    public void setActivarUsuarioAsunto(String activarUsuarioAsunto) {
        this.activarUsuarioAsunto = activarUsuarioAsunto;
    }

    public String getActivarUsuarioMensaje() {
        return activarUsuarioMensaje;
    }

    public void setActivarUsuarioMensaje(String activarUsuarioMensaje) {
        this.activarUsuarioMensaje = activarUsuarioMensaje;
    }

    public String getActualizarUsuarioAsunto() {
        return actualizarUsuarioAsunto;
    }

    public void setActualizarUsuarioAsunto(String actualizarUsuarioAsunto) {
        this.actualizarUsuarioAsunto = actualizarUsuarioAsunto;
    }

    public String getActualizarUsuarioMensaje() {
        return actualizarUsuarioMensaje;
    }

    public void setActualizarUsuarioMensaje(String actualizarUsuarioMensaje) {
        this.actualizarUsuarioMensaje = actualizarUsuarioMensaje;
    }

    public String getActivarUsuarioVinculo() {
        return activarUsuarioVinculo;
    }

    public void setActivarUsuarioVinculo(String activarUsuarioVinculo) {
        this.activarUsuarioVinculo = activarUsuarioVinculo;
    }

    public String getActualizarUsuarioVinculo() {
        return actualizarUsuarioVinculo;
    }

    public void setActualizarUsuarioVinculo(String actualizarUsuarioVinculo) {
        this.actualizarUsuarioVinculo = actualizarUsuarioVinculo;
    }

    public String getUsuarioActivo() {
        return usuarioActivo;
    }

    public void setUsuarioActivo(String usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public String getUsuarioInactivo() {
        return usuarioInactivo;
    }

    public void setUsuarioInactivo(String usuarioInactivo) {
        this.usuarioInactivo = usuarioInactivo;
    }

    public String getUsuarioPendiente() {
        return usuarioPendiente;
    }

    public void setUsuarioPendiente(String usuarioPendiente) {
        this.usuarioPendiente = usuarioPendiente;
    }

    public String getUsuarioBloqueado() {
        return usuarioBloqueado;
    }

    public void setUsuarioBloqueado(String usuarioBloqueado) {
        this.usuarioBloqueado = usuarioBloqueado;
    }

    public String getActualizarDobleVerificacionAsunto() {
        return actualizarDobleVerificacionAsunto;
    }

    public void setActualizarDobleVerificacionAsunto(String actualizarDobleVerificacionAsunto) {
        this.actualizarDobleVerificacionAsunto = actualizarDobleVerificacionAsunto;
    }

    public String getActualizarDobleVerificacionMensaje() {
        return actualizarDobleVerificacionMensaje;
    }

    public void setActualizarDobleVerificacionMensaje(String actualizarDobleVerificacionMensaje) {
        this.actualizarDobleVerificacionMensaje = actualizarDobleVerificacionMensaje;
    }

}
