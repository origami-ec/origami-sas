package org.sas.gateway.utils;

import org.sas.gateway.models.SessionIdentifier;
import org.sas.gateway.models.SessionUser;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;


public class Utils {

    private static final String[] STATIC_FILE_EXTENSIONS = {
            ".css", ".jpg", ".png", ".gif", ".js", ".ttf", ".ico", ".woff2"
    };

    private static final Pattern STATIC_RESOURCE_PATTERN =
            Pattern.compile(".*(css|jpg|png|gif|js|ttf|ico|woff2)(\\.xhtml)?$");

    public static HttpCookie getCookie(ServerHttpRequest request, String appKey) {
        List<HttpCookie> ck = request.getCookies().get(appKey);
        if (ck != null) {
            return ck.get(0);
        } else {
            return null;
        }
    }

    public static HttpCookie getCookieResponse(org.springframework.http.server.reactive.ServerHttpResponse response, String appKey) {
        List<ResponseCookie> ck = response.getCookies().get(appKey);
        if (ck != null) {
            System.out.println("Size " + ck.size());
            ResponseCookie cookie = ck.get(0);
            HttpCookie c = new HttpCookie(cookie.getName(), cookie.getValue());
            return c;
        } else {
            return null;
        }
    }

    public static boolean isEmpty(Collection l) {
        if (l == null) {
            return true;
        } else return l.size() == 0;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isNotEmpty(Collection l) {
        return !Utils.isEmpty(l);
    }

    public static boolean isStaticResource(String url) {
        return Arrays.stream(STATIC_FILE_EXTENSIONS)
                .anyMatch(url::endsWith);
    }

    public static boolean isStaticResourceAny(String url) {
        if(url.contains("resource") || url.contains("/resources")){
            return true;
        }
        return STATIC_RESOURCE_PATTERN.matcher(url).matches() ||
                isStaticResource(url);
    }

    public static Mono<Void> updateModulePathAndCookies(ServerWebExchange exchange, Mono<WebSession> webSessionMono, SessionUser user, String newModulePath, String APP_KEY) {
        boolean isModulePathChanged = !user.getPathModulo().equalsIgnoreCase(newModulePath);

        if (!isModulePathChanged) {
            return Mono.empty();
        }

        return webSessionMono.flatMap(session -> {
            try {
                session.getAttributes().put(session.getId(), user.getAppKey());
                return Mono.empty();
            } catch (Exception e) {
                System.out.println("Error actualizando cookies de sesion: updateModulePathAndCookies " + e.getMessage());
                e.printStackTrace();
                return Mono.error(e);
            }
        });
    }


    public static void updateModulePathAndCookies(ServerWebExchange exchange, WebSession session, SessionUser user, String newModulePath, String APP_KEY) {
        boolean isModulePathChanged = !user.getPathModulo().equalsIgnoreCase(newModulePath);
        try {
            if (isModulePathChanged) {
                session.getAttributes().put(session.getId(), user.getAppKey());
                String appKey = user.getAppKey();
                if (appKey != null) {
                    MultiValueMap<String, ResponseCookie> responseCookies = exchange.getResponse().getCookies();
                    responseCookies.remove(APP_KEY);
                    ResponseCookie newCookie = ResponseCookie.from(APP_KEY, appKey).build();
                    responseCookies.add(APP_KEY, newCookie);
                }
            }
        } catch (Exception e) {
            System.out.println("Error actualizando cookies de sesion: updateModulePathAndCookies " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static String hash(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error al crear hash SHA-256, usando alternativa simple" + e.getMessage());
            return String.valueOf(Math.abs(input.hashCode()));
        }
    }

    public static SessionIdentifier getSessionIdentifier(ServerHttpRequest request) {
        String ip = Optional.ofNullable(request.getRemoteAddress())
                .map(address -> address.getAddress().getHostAddress())
                .orElse("unknown");

        String userAgent = request.getHeaders()
                .getFirst("User-Agent");

        return new SessionIdentifier(ip, userAgent);
    }

    public static String generateSessionKey(SessionIdentifier sessionId) {
        return String.format("session_%s_%s",
                sessionId.getIp(),
                Utils.hash(sessionId.getUserAgent()));
    }

    public static boolean isEmptyString(String l) {
        return l == null || l.isEmpty();
    }


    public static boolean isNotEmptyString(String l) {
        return !Utils.isEmptyString(l);
    }

}



