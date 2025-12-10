package org.sas.seguridad.service.commons;

import org.sas.seguridad.conf.AppProps;
import org.sas.seguridad.dto.RespuestaWs;
import org.sas.seguridad.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RestService {
    private static final Logger logger = Logger.getLogger(RestService.class.getName());
    @Autowired
    private AppProps appProps;
    @Autowired
    private RestTemplate restTemplate;

    public Object restPOST(String url, String token, Object object, Class resultClazz) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (token != null) {
                headers.setBearerAuth(token);
            }
            HttpEntity<Object> requestEntity = new HttpEntity<>(object, headers);
            ResponseEntity<Object> response = restTemplate.exchange(url,
                    HttpMethod.POST, requestEntity, resultClazz);
            return response.getBody();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "restPOST", e);
            return null;
        }
    }

    public Object restGET(String url, String token, Class resultClazz) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (token != null) {
                headers.setBearerAuth(token);
            }
            HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<Object> response = restTemplate.exchange(url,
                    HttpMethod.GET, requestEntity, resultClazz);
            return response.getBody();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "restPOST", e);
            return null;
        }
    }

    public String secuencia(String codigo) {
        RespuestaWs secuencia = (RespuestaWs) restPOST(appProps.getAdministrativo() + "configuraciones/secuencia/generarSecuencia", null, new RespuestaWs(codigo), RespuestaWs.class);
        if(secuencia!=null && secuencia.getEstado()){
            if(!Utils.isEmptyString(secuencia.getMensaje())){
                return secuencia.getMensaje();
            }else{
                Random aleatorio = new Random(System.currentTimeMillis());
                int rangoCodigo = 999999999;
                return String.valueOf(aleatorio.nextInt(rangoCodigo));
            }
        }else{
            Random aleatorio = new Random(System.currentTimeMillis());
            int rangoCodigo = 999999999;
            return String.valueOf(aleatorio.nextInt(rangoCodigo));
        }
    }
}
