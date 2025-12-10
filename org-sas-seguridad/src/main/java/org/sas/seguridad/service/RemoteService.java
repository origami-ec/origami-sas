package org.sas.seguridad.service;


import org.sas.seguridad.conf.AppProps;
import org.sas.seguridad.dto.Correo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RemoteService {
    private static final Logger logger = Logger.getLogger(RemoteService.class.getName());
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AppProps appProps;

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

    @Async
    public void enviarCorreo(Correo correo) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Correo> requestEntity = new HttpEntity<>(correo, headers);
            ResponseEntity<String> response = restTemplate.exchange(appProps.getUrlZuul() + "/servicios-mail/sas/api/enviarCorreo",
                    HttpMethod.POST, requestEntity, String.class);
            logger.log(Level.INFO, "STATUS CORREO " + response.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
