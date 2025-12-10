package org.sas.gateway.conf;

import org.sas.gateway.utils.Utils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

@Configuration
public class ResponseHeaderFilter implements WebFilter {

    @Autowired
    private CacheApplicationUrl cache;
    @Value("${server.servlet.context-path}")
    private String servletContextPath;
    @Value("${logging.level.ROOT}")
    private String logging;
    private String APP_KEY = "Application-Id";

    Logger LOG = Logger.getLogger(ResponseHeaderFilter.class.getName());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            ServerHttpRequest request = exchange.getRequest();
            InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
            String currentUrl = request.getURI().toString();
            String p = request.getPath().value();

            //System.out.println("ResponseHeaderFilter.filter de resources " + currentUrl);
            if ((Utils.isStaticResourceAny(currentUrl))) {
                String rs = servletContextPath + p;
                request = request.mutate().path(rs).build();
                return chain.filter(exchange.mutate().request(request).build());
            }
            if (!currentUrl.contains(servletContextPath) && !"/".equalsIgnoreCase(p)) {
                String rs = servletContextPath + p;
                request = request.mutate().path(rs).build();
                return chain.filter(exchange.mutate().request(request).build());
            }
            if (Utils.isStaticResource(currentUrl)) {
                String rs = servletContextPath + p;
                request = request.mutate().path(rs).build();
                return chain.filter(exchange.mutate().request(request).build());
            }

            System.out.println("ResponseHeaderFilter Origen " + remoteAddress + " Url: " + currentUrl);
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass().getName()).error("Error al filtar url", e);
        }
        return chain.filter(exchange);
    }
}
