package org.sas.administrativo.service.commons;

import org.sas.administrativo.conf.AppProps;
import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.dto.documental.ArchivoDocs;
import org.sas.administrativo.dto.documental.ArchivoIndexDto;
import org.sas.administrativo.dto.documental.UsuarioDocs;
import org.sas.administrativo.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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

    public Object restGET(String url, Class resultClazz) {
        return restGET(url, null, resultClazz);
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

    public ArchivoDocs guardarArchivo(UsuarioDocs usuario,
                                      String contentType, String nameFile, byte[] content,
                                      ArchivoIndexDto archivoIndex) {
        String nombreArchivo = nameFile;
        try {
            nombreArchivo = new String(nameFile
                    .getBytes(Charset.defaultCharset()), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            nombreArchivo = nameFile;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("document")
                .filename(nombreArchivo)
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(content, fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("document", fileEntity);
        body.add("usuario", Utils.gsonTransform(usuario));
        body.add("formato", contentType);
        body.add("indexacion", Utils.gsonTransform(archivoIndex));

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);
        try {
            System.out.println(appProps.getUrlDocumental() + "archivo/cargarDocumento");
            ResponseEntity<ArchivoDocs> response = restTemplate.exchange(
                    appProps.getUrlDocumental() + "archivo/cargarDocumento",
                    HttpMethod.POST,
                    requestEntity,
                    ArchivoDocs.class);
            if (response != null) {
                return response.getBody();
            }
            return null;
        } catch (HttpClientErrorException e) {
            logger.log(Level.SEVERE, "guardarArchivo", e);
        }
        return null;
    }
}
