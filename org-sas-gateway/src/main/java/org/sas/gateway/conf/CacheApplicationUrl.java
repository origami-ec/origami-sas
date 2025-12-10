package org.sas.gateway.conf;

import org.sas.gateway.models.SessionUser;
import org.sas.gateway.utils.EliminarSession;
import org.sas.gateway.utils.RespuestaWs;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.WeakHashMap;

@Service
public class CacheApplicationUrl {

    private WeakHashMap<String, String> mapCache = new WeakHashMap<>();
    private WeakHashMap<String, SessionUser> cacheSession = new WeakHashMap<>();

    public String getUrl(String key, InetSocketAddress remoteAddress) {
        return mapCache.get(remoteAddress + "||" + key);
    }

    public boolean containsKey(String key) {
        return mapCache.containsKey(key);
    }

    public boolean containsSession(String key) {
        return cacheSession.containsKey(key);
    }

    public String add(String key, String value) {
        return mapCache.put(key, value);
    }

    public SessionUser addSession(String key, SessionUser value) {
        return cacheSession.put(key, value);
    }

    public SessionUser getSession(String key) {
        return cacheSession.get(key);
    }

    public SessionUser delSession(String key) {
        return cacheSession.remove(key);
    }

    public WeakHashMap<String, String> getMapCache() {
        return mapCache;
    }

    /**
     * Elimina los datos que coincidan con el appkey
     *
     * @param session
     * @return
     */
    public RespuestaWs clearSession(EliminarSession session) {
        if (session != null) {
            WeakHashMap<String, String> temp = new WeakHashMap<>();
            mapCache.forEach((key, value) -> {
                if (session.getAppKey().equals(value)) {
                    temp.put(key, value);
                }
            });
            if (temp.size() > 0) {
                temp.forEach((key, value) -> {
                    mapCache.remove(key, value);
                });
            }
            return new RespuestaWs("Datos eliminados: " + temp.size(), "GW001", true);
        }
        return new RespuestaWs("No hay datos para eliminar.", "GW002", true);
    }

    public EliminarSession getSession(EliminarSession session, ServerHttpRequest request) {
        if (session != null) {
            WeakHashMap<String, String> temp = new WeakHashMap<>();
            for (Map.Entry<String, String> entry : mapCache.entrySet()) {
                String key = request.getRemoteAddress() + "||" + session.getUrl();
                System.out.println("key " + entry.getKey() + " Url " + key);
                if (key.equals(entry.getKey())) {
                    session.setAppKey(entry.getKey());
                }
            }
            mapCache.forEach((key, value) -> {
                System.out.println("key " + key + " Url " + session.getUrl());
                if (session.getUrl().equals(key)) {
                    session.setAppKey(value);
                }
            });
        }
        return session;
    }

    public WeakHashMap<String, SessionUser> getSessions() {
        return cacheSession;
    }
}
