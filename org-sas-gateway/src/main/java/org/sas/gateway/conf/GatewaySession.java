package org.sas.gateway.conf;

import org.sas.gateway.models.SessionIdentifier;
import org.sas.gateway.models.SessionUser;
import org.sas.gateway.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Configuration
@Order(0)
@Primary
public class GatewaySession implements GlobalFilter, Ordered {

    private static final String APP_KEY = "Application-Id";
    private static final int MAX_SESSION_IDLE_MINUTES = 180;

    @Autowired
    private CacheApplicationUrl cache;

    @Value("${server.servlet.context-path}")
    private String servletContextPath;

    @Value("${session.ip.blacklist:}")
    private List<String> ipBlacklist;

    @Value("${session.max-per-ip:10}")
    private int maxSessionsPerIp;

    @Override
    //@RateLimiter(name = "gatewayRateLimiter")
    public Mono<Void> filter(ServerWebExchange exchange,
                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        try {
            ServerHttpRequest request = exchange.getRequest();
            String currentUrl = request.getURI().getPath();
            return Mono.defer(() -> processRequest(exchange, chain))
                    .onErrorResume(e -> handleError(exchange, e));
        } catch (Exception e) {
            e.printStackTrace();
            return handleError(exchange, e);
        }
    }

    private Mono<Void> processRequest(ServerWebExchange exchange,
                                      org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String currentUrl = request.getURI().getPath();
        if (currentUrl.contains(";")) {
            currentUrl = currentUrl.split(";")[0];
        }
        if (Utils.isStaticResourceAny(currentUrl)) {
            return chain.filter(exchange);
        }

        // Verificar IP en lista negra
        SessionIdentifier sessionId = Utils.getSessionIdentifier(request);
        boolean isLoginRequest = currentUrl.contains("/iniciarSesion");
        System.out.println("currentUrl " + currentUrl + " isLoginRequest " + isLoginRequest);

        if (isIpBlacklisted(sessionId.getIp())) {
            System.out.println("    isIpBlacklisted ip " + sessionId.getIp());
            return rejectRequest(exchange, HttpStatus.FORBIDDEN, "IP blocked");
        }
        String urlModulo = extractModulePath(currentUrl);

        return exchange.getSession()
                .flatMap(session -> handleSession(exchange, chain, sessionId, urlModulo, isLoginRequest))
                .switchIfEmpty(chain.filter(exchange));
    }


    private String extractModulePath(String currentUrl) {
        if (!currentUrl.startsWith(servletContextPath)) {
            return "/";
        }
        try {
            String path = currentUrl.replace(servletContextPath + "/", "");
            return path.split("/")[0];
        } catch (Exception e) {
            System.out.println("    Error extracting module path from URL: " + currentUrl + " Error " + e.getMessage());
            return "/";
        }
    }

    private Mono<Void> handleSession(ServerWebExchange exchange,
                                     org.springframework.cloud.gateway.filter.GatewayFilterChain chain,
                                     SessionIdentifier sessionId,
                                     String urlModulo,
                                     boolean isLoginRequest) {
        try {
            String sessionKey = generateSessionKey(sessionId);

            // Limpiar sesiones antiguas
            cleanExpiredSessions(sessionId.getIp());

            // Verificar límite de sesiones por IP
            if (!isLoginRequest && !cache.containsSession(sessionKey) &&
                    getSessionCountForIp(sessionId.getIp()) >= maxSessionsPerIp) {
                return rejectRequest(exchange, HttpStatus.TOO_MANY_REQUESTS,
                        "Maximum sessions per IP reached");
            }

            SessionUser user = getOrCreateSessionUser(sessionKey, exchange, urlModulo, isLoginRequest);
            updateUserSession(user, exchange, urlModulo, isLoginRequest);

            return proceedWithRequest(exchange, chain, user, isLoginRequest);
        } catch (Exception e) {
            System.out.println("    Error handling session: " + e.getMessage());
            e.printStackTrace();
            return rejectRequest(exchange, HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error processing request");
        }
    }

    private String generateSessionKey(SessionIdentifier sessionId) {
        return Utils.generateSessionKey(sessionId);
    }

    private SessionUser getOrCreateSessionUser(String sessionKey,
                                               ServerWebExchange exchange,
                                               String urlModulo, boolean isLoginRequest) {
        System.out.println("    Session " + sessionKey + " existe " + cache.containsSession(sessionKey));
        if (cache.containsSession(sessionKey)) {
            SessionUser user = cache.getSession(sessionKey);
            System.out.println("    Update session " + user.getId());
            updateSessionTimestamp(user);
            if (!isLoginRequest) {
                if (user.getAppKey() == null) {
                    HttpCookie cookieResponse = Utils.getCookieResponse(exchange.getResponse(), APP_KEY);
                    if (cookieResponse != null && Utils.isNotEmptyString(cookieResponse.getValue())) {
                        System.out.println("    Update Key form response " + user.getAppKey());
                        user.setAppKey(cookieResponse.getValue());
                    } else {
                        HttpCookie cookie = Utils.getCookie(exchange.getRequest(), APP_KEY);
                        if (cookie != null && Utils.isNotEmptyString(cookie.getValue())) {
                            System.out.println("    Update Key form request " + user.getAppKey());
                            user.setAppKey(cookie.getValue());
                        }
                    }
                }
            }
            return user;
        }
        SessionUser newUser = new SessionUser();
        newUser.setId(sessionKey);
        newUser.setPathModulo(urlModulo);
        newUser.setUsername(sessionKey);
        newUser.setLastUrl(exchange.getRequest().getURI().getPath());
        newUser.setLastAccessTime(LocalDateTime.now());

        return newUser;
    }

    private void updateUserSession(SessionUser user,
                                   ServerWebExchange exchange,
                                   String urlModulo,
                                   boolean isLoginRequest) {
        if (isLoginRequest) {
            System.out.println("    delSession: " + user.getId() + " isLoginRequest " + isLoginRequest);
            cache.delSession(user.getId());
            removeCookieIfExists(exchange, APP_KEY, user);
//            moduloService.closeSession(user);
        } else {
            updateSessionTimestamp(user);
            boolean isModulePathChanged = !user.getPathModulo().equalsIgnoreCase(urlModulo);
            System.out.println("    updateUserSession isModulePathChanged: " + isModulePathChanged + " pathModulo " + user.getPathModulo() + " urlModulo " + urlModulo);
            if (isModulePathChanged) {
                tryUpdateAppKeyResponse(user, exchange.getResponse());
            }
            Utils.updateModulePathAndCookies(exchange,
                    exchange.getSession(),
                    user,
                    urlModulo,
                    APP_KEY);
            cache.addSession(user.getId(), user);
        }
    }

    private void removeCookieIfExists(ServerWebExchange exchange, String cookieName, SessionUser user) {
        if (exchange.getRequest().getCookies().containsKey(cookieName)) {
            removeCookie(exchange, cookieName, user);
            System.out.println("    Removed cookie: " + cookieName);
        }
        removeCookieResponse(exchange, cookieName, user);
    }

    private void removeCookieResponse(ServerWebExchange exchange, String cookieName, SessionUser user) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .path("/" + user.getPathModulo())
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .build();
        exchange.getResponse().addCookie(cookie);
    }

    private ServerHttpRequest removeCookie(ServerWebExchange exchange, String cookieName, SessionUser user) {
        return exchange.getRequest().mutate()
                .headers(httpHeaders -> {
                    List<String> cookies = httpHeaders.get("Cookie");
                    if (cookies != null) {
                        List<String> newCookies = cookies.stream()
                                .flatMap(cookie -> Arrays.stream(cookie.split(";")))
                                .map(String::trim)
                                .filter(cookie -> !cookie.startsWith(cookieName + "="))
                                .collect(Collectors.toList());
                        System.out.println("    removeCookie new Cookie " + newCookies);
                        if (!newCookies.isEmpty()) {
                            httpHeaders.set("Cookie", String.join("; ", newCookies));
                        } else {
                            httpHeaders.remove("Cookie");
                        }
                    }
                })
                .build();

    }

    private void updateSessionTimestamp(SessionUser user) {
        user.setLastAccessTime(LocalDateTime.now());
    }

    private void tryUpdateAppKeyResponse(SessionUser user, ServerHttpResponse response) {
        if (user.getAppKey() != null) {
            ResponseCookie cookie = ResponseCookie.from(APP_KEY, user.getAppKey())
                    .path("/" + user.getPathModulo())
                    .httpOnly(true)
                    .secure(true)
                    .build();
            if (response.getCookies().containsKey(APP_KEY)) {
                response.getCookies().set(APP_KEY, cookie);
            } else {
                response.addCookie(cookie);
            }
        }
    }

    private void cleanExpiredSessions(String ip) {
        cache.getSessions().entrySet().removeIf(entry -> {
            SessionUser user = entry.getValue();
            return user.getId().startsWith("session_" + ip) &&
                    user.getLastAccessTime()
                            .plusMinutes(MAX_SESSION_IDLE_MINUTES)
                            .isBefore(LocalDateTime.now());
        });
    }

    private int getSessionCountForIp(String ip) {
        return (int) cache.getSessions().values().stream()
                .filter(user -> user.getId().startsWith("session_" + ip))
                .count();
    }

    private boolean isIpBlacklisted(String ip) {
        return ipBlacklist.contains(ip);
    }

    private Mono<Void> proceedWithRequest(ServerWebExchange exchange,
                                          GatewayFilterChain chain,
                                          SessionUser user, boolean isLoginRequest) {
        ServerHttpRequest newRequest = exchange.getRequest()
                .mutate()
                .header("X-Session-User", user.getId())
                .headers(httpHeaders -> {
                    if (isLoginRequest) {
                        List<String> cookies = httpHeaders.get("Cookie");
                        if (cookies != null) {
                            List<String> newCookies
                                    = cookies.stream()
                                    .flatMap(cookie -> Arrays.stream(cookie.split(";")))
                                    .map(String::trim)
                                    .filter(cookie -> !cookie.startsWith(APP_KEY + "="))
                                    .collect(Collectors.toList());
                            System.out.println("    proceedWithRequest new Cookie " + newCookies);
                            if (!newCookies.isEmpty()) {
                                httpHeaders.set("Cookie", String.join("; ", newCookies));
                            } else {
                                httpHeaders.remove("Cookie");
                            }
                        }

                    }
                })
                .build();

        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    private Mono<Void> handleError(ServerWebExchange exchange, Throwable error) {
        System.out.println("    Error in gateway filter " + error.getMessage());
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        error.printStackTrace();
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> rejectRequest(ServerWebExchange exchange,
                                     HttpStatus status,
                                     String reason) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("X-Rejection-Reason", reason);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -100;
    }
}