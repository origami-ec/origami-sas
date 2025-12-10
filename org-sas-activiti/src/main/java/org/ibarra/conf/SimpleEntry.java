package org.ibarra.conf;

import com.fasterxml.jackson.databind.deser.std.MapEntryDeserializer;

import java.util.Map;

/**
 * Simple Entry<br>
 * <br>
 * Intentionally does not implement the {@link Map.Entry} interface, so as not
 * to invoke the default deserializer {@link MapEntryDeserializer}.
 *
 * @author Gitesh Agarwal (gagarwa)
 */
public class SimpleEntry<K, V> {

    private K key;

    private V value;

    /**
     * Default Constructor
     */
    public SimpleEntry() {

    }

    public SimpleEntry(K key) {
        this.key = key;
    }

    public SimpleEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @param key the key
     */
    public void setKey(K key) {
        this.key = key;
    }

    public K getKey() {
        return key;
    }

    /**
     * @param value the value
     */
    public void setValue(V value) {
        this.value = value;
    }

    public V getValue() {
        return value;
    }

}