package org.sas.mail.services;

import org.sas.mail.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Rest service.
 */
@Service
public class RestService {
    private static final Logger logger = Logger.getLogger(RestService.class.getName());

    @Autowired
    private ApplicationProperties appProps;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Rest post object.
     *
     * @param url         the url
     * @param token       the token
     * @param object      the object
     * @param resultClazz the result clazz
     * @return the object
     */
    public <T> T restPOST(String url, String token, Object object, Class resultClazz, HttpHeaders headers) {
        try {
            logger.info(url);

            if (headers == null) headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (token != null) {
                headers.setBearerAuth(token);
            }
            HttpEntity<Object> requestEntity = new HttpEntity<>(object, headers);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, resultClazz);
            return response.getBody();
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

    public ResponseEntity<Object> restEntityPOST(String url, String token, Object object, Class resultClazz) {
        ResponseEntity<Object> response = null;
        try {
            logger.info(url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (token != null) {
                headers.setBearerAuth(token.replace("Bearer ", ""));
            }
            HttpEntity<Object> requestEntity = new HttpEntity<>(object, headers);
            response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, resultClazz);
        } catch (HttpMessageNotReadableException e) {
            System.out.println("Executando metodo Post Url: " + url /*+ " Data: " + object*/);
            System.out.println("Error " + e.getMessage());
        } catch (ResourceAccessException e) {
            System.out.println("Tiempo de respuesta Excedido " + url + " data: " + object);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "restPOST url: " + url + " data: " + object, e);
        }
        return response;
    }


    public <T> T restPOST(String url, Object object, Class resultClazz) {
        return restPOST(url, null, object, resultClazz, null);
    }

    public <T> T restPOSTHeaders(String url, Object object, Class resultClazz, HttpHeaders headers) {
        return restPOST(url, null, object, resultClazz, headers);
    }

    public List restPOSTList(String url, String token, Object object, Class resultClazz) {
        try {
            logger.info(url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (token != null) {
                headers.setBearerAuth(token);
            }
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

    public <T> T restGET(String url, Class resultClazz) {
        return restGET(url, null, resultClazz);
    }


    public <T> T restGET(String url, String token, Class resultClazz) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (token != null) {
                headers.setBearerAuth(token);
            }
            HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<T> response = restTemplate.exchange(url,
                    HttpMethod.GET, requestEntity, resultClazz);
            if (response.hasBody()) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {
            //logger.log(Level.SEVERE, "restPOST {" + url + "}", e);
            return null;
        }
    }


    public List restListPOST(String url, String token, Object object, Class resultClazz) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (token != null) {
                headers.setBearerAuth(token);
            }
            HttpEntity<Object> requestEntity = new HttpEntity<>(object, headers);
            ResponseEntity<Object[]> response = restTemplate.exchange(url,
                    HttpMethod.POST, requestEntity, resultClazz);
            if (response != null) {
                return Arrays.asList(response.getBody());
            }
        } catch (RestClientException e) {
            logger.log(Level.SEVERE, "restPOST", e);
        }
        return null;
    }

    public List methodListGET(String url, String token, Class resultClazz) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);


            if (token != null) {
                headers.setBearerAuth(token);
            }

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Object[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, resultClazz);
            if (response != null) {
                return Arrays.asList(response.getBody());
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "restPOST {" + url + "}", e);
            return null;
        }
    }

}
