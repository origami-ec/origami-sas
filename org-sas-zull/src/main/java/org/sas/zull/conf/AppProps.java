package org.sas.zull.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 3)
public class AppProps {

    @Value("${server.port}")
    private String port;
    @Value("${app.activeDirectory.usuario}")
    private String adUsuario;
    @Value("${app.activeDirectory.clave}")
    private String adClave;
    @Value("${app.activeDirectory.dn}")
    private String adDn;

    @Value("${app.activeDirectory.host}")
    private String adHost;
    @Value("${app.activeDirectory.port}")
    private String adPort;
    @Value("${app.activeDirectory.auth}")
    private String adAuth;
    @Value("${app.activeDirectory.referral}")
    private String adReferral;
    @Value("${app.activeDirectory.context}")
    private String adContext;


    private String urlApp;
    private Integer intentosSesion;
    private String usuarioActivo;
    private String usuarioInactivo;
    private String usuarioPendiente;
    private String usuarioBloqueado;

    public AppProps() {
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Integer getIntentosSesion() {
        return intentosSesion;
    }

    public void setIntentosSesion(Integer intentosSesion) {
        this.intentosSesion = intentosSesion;
    }

    public String getUrlApp() {
        return urlApp;
    }

    public void setUrlApp(String urlApp) {

        this.urlApp = urlApp;
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

    public String getAdHost() {
        return adHost;
    }

    public void setAdHost(String adHost) {
        this.adHost = adHost;
    }

    public String getAdPort() {
        return adPort;
    }

    public void setAdPort(String adPort) {
        this.adPort = adPort;
    }

    public String getAdAuth() {
        return adAuth;
    }

    public void setAdAuth(String adAuth) {
        this.adAuth = adAuth;
    }

    public String getAdReferral() {
        return adReferral;
    }

    public void setAdReferral(String adReferral) {
        this.adReferral = adReferral;
    }

    public String getAdContext() {
        return adContext;
    }

    public String getAdUsuario() {
        return adUsuario;
    }

    public String getAdClave() {
        return adClave;
    }

    public String getAdDn() {
        return adDn;
    }
}
