package org.ibarra.media.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProps {

    @Value("${app.urlResource}")
    private String urlResource;
    @Value("${app.imagenes}")
    private String imagenes;
    @Value("${app.archivos}")
    private String archivos;
    @Value("${app.archivoFirmados}")
    private String archivoFirmados;
    @Value("${app.manuales}")
    private String manuales;
    @Value("${app.documental}")
    private String documental;
    @Value("${app.notas}")
    private String notas;
    @Value("${app.talentoHumanoFormato}")
    private String talentoHumanoFormato;

    public AppProps() {
    }

    public String getArchivos() {
        return archivos;
    }

    public void setArchivos(String archivos) {
        this.archivos = archivos;
    }

    public String getUrlResource() {
        return urlResource;
    }

    public void setUrlResource(String urlResource) {
        this.urlResource = urlResource;
    }

    public String getImagenes() {
        return imagenes;
    }

    public void setImagenes(String imagenes) {
        this.imagenes = imagenes;
    }

    public String getArchivoFirmados() {
        return archivoFirmados;
    }

    public void setArchivoFirmados(String archivoFirmados) {
        this.archivoFirmados = archivoFirmados;
    }

    public String getDocumental() {
        return documental;
    }

    public void setDocumental(String documental) {
        this.documental = documental;
    }

    public String getManuales() {
        return manuales;
    }

    public void setManuales(String manuales) {
        this.manuales = manuales;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getTalentoHumanoFormato() {
        return talentoHumanoFormato;
    }

    public void setTalentoHumanoFormato(String talentoHumanoFormato) {
        this.talentoHumanoFormato = talentoHumanoFormato;
    }
}
