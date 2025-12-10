package org.sas.gateway.service;

import org.sas.gateway.entity.RuteoModulo;
import org.sas.gateway.models.SessionCloseMessage;
import org.sas.gateway.models.SessionUser;
import org.sas.gateway.repository.RuteoModuloRepo;
import org.sas.gateway.utils.RespuestaWs;
import org.sas.gateway.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
public class RuteoModuloService {

    @Autowired
    private RuteoModuloRepo ruteoModuloRepo;
    @Value("${spring.profiles.active}")
    private String profile;
    private RestTemplate restTemplate;
    private int connectionTimeout = 30000;
    private int readTimeout = 30000;

    @Value("${session.close.timeout:30}")
    private int sessionCloseTimeout;


    public List<RuteoModulo> findAll(RuteoModulo model) {
        List<RuteoModulo> list;
        if (model == null) {
            list = ruteoModuloRepo.findAll();
        } else {
            list = ruteoModuloRepo.findAll(Example.of(model));
        }
        for (RuteoModulo rm : list) {
            if (profile.equals("prod")) {
                rm.setHostApp(rm.getHostAppProd());
            }
            if (profile.equals("pre")) {
                rm.setHostApp(rm.getHostAppTest());
            }
        }
        return list;
    }

    public List<RuteoModulo> findAllModulosByEstado() {
        List<RuteoModulo> list;
        list = ruteoModuloRepo.findAllByEstadoAndAmbiente(Boolean.TRUE, profile);
        for (RuteoModulo rm : list) {
            if (profile.equals("prod")) {
                rm.setHostApp(rm.getHostAppProd());
            }
            if (profile.equals("pre")) {
                rm.setHostApp(rm.getHostAppTest());
            }
        }
        return list;
    }

    /**
     * @param url
     * @param token
     * @param object
     * @param resultClazz
     * @param request     Si viene este parametro se procede a copiar todos los headers para indicar que viene desde otra peticion
     * @return
     */
    public Object restPOST(String url, String token, Object object, Class resultClazz, ServerHttpRequest request) {
        try {
            System.out.println("restPOST Url " + url);
            HttpHeaders headers = new HttpHeaders();
            if (token != null) {
                headers.setBearerAuth(token);
            }
            if (request != null) {
                request.getHeaders().forEach((key, values) -> headers.addAll(key, values));
                System.out.println("Copiando headers desde request ");
            }
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(connectionTimeout);
            factory.setReadTimeout(readTimeout);

            restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(factory);

            HttpEntity<Object> requestEntity = new HttpEntity<>(object, headers);
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, resultClazz);
            return response.getBody();
        } catch (HttpMessageNotReadableException e) {
            System.out.println("Executando metodo Post Url: " + url /*+ " Data: " + object*/);
            System.out.println("Error " + e.getMessage());
        } catch (ResourceAccessException e) {
            System.out.println("Tiempo de respuesta Excedido " + url + " data: " + object);
        } catch (Exception e) {
            System.out.println("restPOST Error " + e.getMessage());
        }
        return null;
    }

    public RespuestaWs closeSession(SessionUser sessionUser, ServerHttpRequest request) {
        try {
            String modulo = sessionUser.getPathModulo();
            System.out.println("sessionUser " + sessionUser);
            RespuestaWs r = new RespuestaWs();
            List<RuteoModulo> modulos = this.findAllModulosByEstado();
            if (Utils.isNotEmpty(modulos)) {
                for (RuteoModulo rm : modulos) {
                    try {
                        if (!rm.getAppName().contains(modulo)) {
                            URI u = new URI(rm.getHostApp());
                            String ur = u.getScheme() + "://" + u.getHost() + ":" + u.getPort() + rm.getAppName().replace("/**", "");
                            ur = ur + (ur.endsWith("/") ? "session/" : "/session/") + sessionUser.getId() + "?moduleOrigin=" + sessionUser.getPathModulo();
                            System.out.println("Cerrando sesion en application: " + rm.getAppName() + " url " + ur);
                            r = (RespuestaWs) restPOST(ur, null, sessionUser, RespuestaWs.class, request);
                            System.out.println("Respuesta de application: " + rm.getAppName() + " respuesta " + r);
                        }
                    } catch (HttpStatusCodeException e) {
                        System.out.println("Error al cerrar sesion 404 en application: " + rm.getAppName() + " respuesta " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Exception al cerrar sesion en application: " + rm.getAppName() + " respuesta " + e.getMessage());
                    }
                }
            }
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //@MessageListener(queue = SESSION_CLOSE_QUEUE)
    public void handleSessionClose(SessionCloseMessage message) {

    }


}
