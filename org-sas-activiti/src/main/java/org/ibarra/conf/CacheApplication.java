package org.ibarra.conf;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CacheApplication {

    private HashMap<String, Object> mapCache = new HashMap<>();

    public Object get(String key) {
        return mapCache.get(key);
    }

    public boolean containsKey(String key) {
        return mapCache.containsKey(key);
    }

    public Object add(String key, Object value) {
        return mapCache.put(key, value);
    }

    public HashMap<String, Object> getMapCache() {
        return mapCache;
    }

    public void setMapCache(HashMap<String, Object> mapCache) {
        this.mapCache = mapCache;
    }

    @Override
    public String toString() {
        return mapCache.toString();
    }
}
