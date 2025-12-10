package org.sas.zull.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RestService {
    private static final Logger logger = Logger.getLogger(RestService.class.getName());
    @Autowired
    private RestTemplate restTemplate;

    public Object restPOST(String url, String token, Object object, Class resultClazz) {
       try{
           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(MediaType.APPLICATION_JSON);
           if (token != null) {
               headers.setBearerAuth(token);
           }
           HttpEntity<Object> requestEntity = new HttpEntity<>(object, headers);
           ResponseEntity<Object> response = restTemplate.exchange(url,
                   HttpMethod.POST, requestEntity, resultClazz);
           return response.getBody();
       }catch (HttpServerErrorException.InternalServerError e){
           System.out.println("Error interno del servicio del cliente.");
           return null;
       }catch (Exception e){
           logger.log(Level.SEVERE, "restPOST", e);
           return null;
       }
    }

}


