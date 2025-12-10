package org.ibarra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RestService {
    private static final Logger logger = Logger.getLogger(RestService.class.getName());

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
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, resultClazz);
            if (resultClazz.isArray()) {
                if (response.getBody() != null) {
                    Object[] array = (Object[]) response.getBody();
                    List list = new ArrayList(Arrays.asList(array));
                    return list;
                }
            } else {
                return response.getBody();
            }
        } catch (Exception e) {
            System.out.println(url + " restPOST " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Object restGET(String url, String token, Class resultClazz) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (token != null) {
                headers.setBearerAuth(token);
            }
            HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, resultClazz);
            return response.getBody();
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }
    public List restPOSTList(String url,   Object object, Class resultClazz) {
        try {
            logger.info(url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> requestEntity = new HttpEntity<>(object, headers);
            ResponseEntity<Object[]> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, resultClazz);
            if (response != null && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            } else {
                return null;
            }
        } catch (HttpMessageNotReadableException e) {
            System.out.println("Executando metodo Post Url: " + url /*+ " Data: " + object*/);
            System.out.println("Error " + e.getMessage());
        } catch (ResourceAccessException e) {
            System.out.println("Tiempo de respuesta Excedido " + url + " data: " + object);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "restPOST url: " + url + " data: " + object, e);
        }
        return null;
    }

}
