package org.sas.mail.config;

import org.sas.mail.entity.CorreoSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${email.ruta_archivos}")
    private String rutaArchivos;

    private CorreoSettings correoParametros;
    private CorreoSettings correoCartera;
    private CorreoSettings correoCoactiva;

    public ApplicationProperties() {
    }

    public Boolean isProd() {
        return activeProfile.equals("prod") || activeProfile.equals("dev");
    }

    public Boolean isDev() {
        return !isProd();
    }


    public String getRutaArchivos() {
        return rutaArchivos;
    }

    public void setRutaArchivos(String rutaArchivos) {
        this.rutaArchivos = rutaArchivos;
    }

    public CorreoSettings getCorreoParametros() {
        return correoParametros;
    }

    public void setCorreoParametros(CorreoSettings correoParametros) {
        this.correoParametros = correoParametros;
    }

    public CorreoSettings getCorreoCartera() {
        return correoCartera;
    }

    public void setCorreoCartera(CorreoSettings correoCartera) {
        this.correoCartera = correoCartera;
    }

    public CorreoSettings getCorreoCoactiva() {
        return correoCoactiva;
    }

    public void setCorreoCoactiva(CorreoSettings correoCoactiva) {
        this.correoCoactiva = correoCoactiva;
    }
}
