package org.sas.administrativo.conf;

import org.springframework.stereotype.Service;

import java.util.WeakHashMap;

@Service
public class CacheApplication {

    private WeakHashMap<String, Object> mapCache = new WeakHashMap<>();

    public Object get(String key) {
        return mapCache.get(key);
    }

    public boolean containsKey(String key) {
        return mapCache.containsKey(key);
    }

    public Object add(String key, Object value) {
        return mapCache.put(key, value);
    }

    public WeakHashMap<String, Object> getMapCache() {
        return mapCache;
    }
}
