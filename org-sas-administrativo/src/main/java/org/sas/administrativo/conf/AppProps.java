package org.sas.administrativo.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProps {

    @Value("${app.urlZuul}")
    private String urlZuul;
    @Value("${app.dinardap.key}")
    private String dinardapKey;
    @Value("${app.dinardap.ip}")
    private String dinardapIp;
    @Value("${app.dinardap.codigoPaquete}")
    private String dinardapCodigoPaquete;
    @Value("${app.dinardap.path.consulta}")
    private String dinardapPathConsulta;
    @Value("${app.dinardap.codigoPaqueteSri}")
    private String dinardapCodigoPaqueteSri;
    @Value("${app.dinardap.codigoPaquetePer}")
    private String dinardapCodigoPaquetePer;
    @Value("${app.urlDocumental}")
    private String urlDocumental;
    @Value("${app.archivos}")
    private String rutaArchivos;

    private Long firmaPendienteId;
    private Long firmaFinalizadaId;
    private Long firmaInactivaId;

    public AppProps() {
    }

    public String getUrlZuul() {
        return urlZuul;
    }

    public String getDinardapKey() {
        return dinardapKey;
    }

    public String getDinardapIp() {
        return dinardapIp;
    }

    public String getDinardapCodigoPaquete() {
        return dinardapCodigoPaquete;
    }

    public String getDinardapPathConsulta() {
        return dinardapPathConsulta;
    }

    public String getDinardapCodigoPaqueteSri() {
        return dinardapCodigoPaqueteSri;
    }

    public String getDinardapCodigoPaquetePer() {
        return dinardapCodigoPaquetePer;
    }

    public String getUrlDocumental() {
        return urlDocumental;
    }

    public void setUrlDocumental(String urlDocumental) {
        this.urlDocumental = urlDocumental;
    }

    public Long getFirmaPendienteId() {
        return firmaPendienteId;
    }

    public void setFirmaPendienteId(Long firmaPendienteId) {
        this.firmaPendienteId = firmaPendienteId;
    }

    public Long getFirmaFinalizadaId() {
        return firmaFinalizadaId;
    }

    public void setFirmaFinalizadaId(Long firmaFinalizadaId) {
        this.firmaFinalizadaId = firmaFinalizadaId;
    }

    public Long getFirmaInactivaId() {
        return firmaInactivaId;
    }

    public void setFirmaInactivaId(Long firmaInactivaId) {
        this.firmaInactivaId = firmaInactivaId;
    }

    public String getRutaArchivos() {
        return rutaArchivos;
    }

    public void setRutaArchivos(String rutaArchivos) {
        this.rutaArchivos = rutaArchivos;
    }
}
