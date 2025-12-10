package org.sas.gateway.conf;


import org.sas.gateway.entity.RuteoModulo;
import org.sas.gateway.models.SessionIdentifier;
import org.sas.gateway.models.SessionUser;
import org.sas.gateway.service.RuteoModuloService;
import org.sas.gateway.utils.RespuestaWs;
import org.sas.gateway.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

@Configuration
@RestController
public class ConfigRuteo {

    @Value("${server.servlet.context-path}")
    private String servletContextPath;
    @Value("${server.port}")
    private String serverPort;
    @Value("${logging.level.ROOT}")
    private String logging;


    @Autowired
    private RuteoModuloService service;
    @Autowired
    private CacheApplicationUrl cache;
    private String APP_KEY = "Application-Id";
    @Value("${spring.profiles.active}")
    private String profile;

    @CrossOrigin(origins = "*")
    @GetMapping(value = "${server.servlet.contextPath}/allModulo")
    public List<RuteoModulo> showAllContextPath() {
        List<RuteoModulo> mods = service.findAllModulosByEstado();
        System.out.println("Modulos cargados >>>" + mods.size());
        if (mods != null) {
            Collections.sort(mods, Comparator.comparing(RuteoModulo::getDescripcion));
        }
        return mods;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/allModulo")
    public List<RuteoModulo> showAll() {
        List<RuteoModulo> mods = service.findAllModulosByEstado();
        System.out.println("Modulos cargados >>>" + mods.size());
        if (mods != null) {
            Collections.sort(mods, Comparator.comparing(RuteoModulo::getDescripcion));
        }
        return mods;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/origamiGT/gateway/clearsession/")
    public RespuestaWs eliminarSession(@RequestBody SessionUser session, ServerHttpRequest request) {
        SessionIdentifier sessionIdentifier = Utils.getSessionIdentifier(request);
        String id = Utils.generateSessionKey(sessionIdentifier);
        SessionUser sessionUser = cache.getSession(id);
        if (sessionUser == null) {
            sessionUser = new SessionUser();
        }
        sessionUser.setUser(session.getUser());
        sessionUser.setAppKey(session.getAppKey());
        sessionUser.setId(id);
        RespuestaWs rw = service.closeSession(session, request);

        return rw;
    }

    @Bean
    WebClient client() {
        return WebClient.builder().build();
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.setContextPath(servletContextPath);
    }

    @Bean
    public RouteLocator appRoutes(RouteLocatorBuilder builder) {
        RuteoModulo fm = new RuteoModulo();
        fm.setEstado(true);
        fm.setAmbiente(profile);
        List<RuteoModulo> mods = service.findAll(fm);
        RouteLocatorBuilder.Builder b = builder.routes();
        b.route(servletContextPath, predicateSpec -> predicateSpec.path(servletContextPath)
                .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath(servletContextPath, "/")).uri("http://localhost:" + serverPort));
        int count = 0;
        if (Utils.isNotEmpty(mods)) {
            for (RuteoModulo rm : mods) {
                try {
                    URI routingTargetWithPath = URI.create(rm.getHostApp());
                    System.out.println("///// --> " + routingTargetWithPath.getPath() + " -> " + rm.getHostApp());
                    String pathRewrite = (servletContextPath + rm.getAppName());
                    b.route(rm.getAppNameUri(), p -> p.path(false, pathRewrite).filters(f -> f.filter((exchange, chain) -> {
                                ServerHttpRequest req = exchange.getRequest();
                                if ("DEBUG".equalsIgnoreCase(logging)) {
                                    System.out.println("\\\\appRoutes AppNameUri " + rm.getAppNameUri() + " pathRewrite " + pathRewrite + " >>> " + req.getURI().getRawPath());
                                }
                                addOriginalRequestUrl(exchange, req.getURI());
                                String path = req.getURI().getRawPath();
                                path = path.replace(servletContextPath, "");
                                if (Utils.isStaticResource(path)
                                        || !Utils.isStaticResourceAny(path)) {
                                    ServerHttpRequest newreq = req.mutate().path(path).build();
                                    return chain.filter(exchange.mutate().request(newreq).build());
                                }
                                if ("DEBUG".equalsIgnoreCase(logging)) {
                                    System.out.println("\\\\appRoutes Concatenar / " + ((!path.endsWith("/") && !path.contains("resource"))) + " ---> path route " + path);
                                }
                                ServerHttpRequest newreq = req.mutate().path(path).build();
                                return chain.filter(exchange.mutate().request(newreq).build());
                            }).rewritePath(pathRewrite.replace("/**", "(?<segment>.*)"), "/$\\{segment}")
                    ).uri(routingTargetWithPath));

                    b.route(rm.getAppNameUri(), predicateSpec -> predicateSpec.path(rm.getAppNameUri())
                            .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath(pathRewrite.replace("/**", "/"), rm.getAppNameUri())).uri(routingTargetWithPath));

                    count++;
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
        System.out.println("\\\\appRoutes Path configurados " + count);
        return b.build();
    }


}
