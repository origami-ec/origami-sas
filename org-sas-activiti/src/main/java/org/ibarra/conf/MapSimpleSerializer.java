package org.ibarra.conf;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.MapEntrySerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapSimpleSerializer<K, V> extends StdSerializer<Map<K, V>> {

    private static final long serialVersionUID = 1L;

    /**
     * Default Constructor
     */
    public MapSimpleSerializer() {
        super(Map.class, false);
    }

    @Override
    public void serialize(Map<K, V> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        //System.out.println("SimpleMapSerializer:: " + value);
        List<SimpleEntry<K, V>> listValues = value.entrySet()
                .stream()
                .map(SimpleEntry::new)
                .collect(Collectors.toList());

        provider.defaultSerializeValue(listValues, gen);
    }

    /**
     * Simple Entry<br>
     * <br>
     * Intentionally does not implement the {@link Map.Entry} interface, so as not
     * to invoke the default serializer {@link MapEntrySerializer}.
     *
     * @author Gitesh Agarwal (gagarwa)
     */
    protected static class SimpleEntry<K, V> {

        private K key;

        private V value;

        /**
         * Default Constructor
         *
         * @param entry the map entry
         */
        public SimpleEntry(Map.Entry<K, V> entry) {
            key = entry.getKey();
            value = entry.getValue();
        }

        /**
         * @return the key
         */
        public K getKey() {
            return key;
        }

        /**
         * @return the value
         */
        public V getValue() {
            return value;
        }

    }

}
