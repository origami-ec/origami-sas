package org.ibarra.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProps {

    @Value("${spring.profiles.active}")
    private String ambiente;
    @Value("${app.urlZuul}")
    private String urlZuul;
    @Value("${app.urlAdministrativo}")
    private String urlAdministrativo;
    @Value("${app.urlCorreo}")
    private String urlCorreo;
    @Value("${app.urlSeguridad}")
    private String urlSeguridad;
    @Value("${app.urlVentanillaExterna}")
    private String urlVentanillaExterna;
    @Value("${app.urlVentanillaInterna}")
    private String urlVentanillaInterna;
    @Value("${app.urlTalentoHumano}")
    private String urlTalentoHumano;
    @Value("${spring.profiles.active}")
    private String perfil;
    @Value("${app.pathBpmn}")
    private String pathBpmn;
    private String correoPrueba = "choez552@gmail.com";
    @Value("${app.imagenes}")
    private String imagenes;
    @Value("${app.reportes}")
    private String reportes;

    public AppProps() {
    }

    public String getAmbiente() {
        return ambiente;
    }

    public String getUrlZuul() {
        return urlZuul;
    }

    public String getUrlAdministrativo() {
        return urlAdministrativo;
    }

    public String getUrlCorreo() {
        return urlCorreo;
    }

    public String getUrlSeguridad() {
        return urlSeguridad;
    }

    public String getPerfil() {
        return perfil;
    }

    public String getPathBpmn() {
        return pathBpmn;
    }

    public boolean esProd() {
        return perfil.equals("prod");
    }

    public String getCorreoPrueba() {
        return correoPrueba;
    }

    public String getImagenes() {
        return imagenes;
    }

    public String getReportes() {
        return reportes;
    }

    public String getUrlVentanillaExterna() {
        return urlVentanillaExterna;
    }

    public String getUrlVentanillaInterna() {
        return urlVentanillaInterna;
    }

    public String getUrlTalentoHumano() {
        return urlTalentoHumano;
    }
}
