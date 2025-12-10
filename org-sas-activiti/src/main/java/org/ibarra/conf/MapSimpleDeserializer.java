package org.ibarra.conf;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapSimpleDeserializer<K, V> extends StdDeserializer<Map<K, V>> {

    private static final long serialVersionUID = 1L;

    private CollectionType type;
    private MapType mapType;

    /**
     * Default Constructor
     *
     * @param type    the map type (key, value)
     * @param factory the type factory, to create the collection type
     */
    public MapSimpleDeserializer(MapType type, TypeFactory factory) {
        super(type.getRawClass());
//        System.out.println("getRawClass " + type.getRawClass() + " getKeyType " + type.getKeyType() + " getContentType " + type.getContentType());
        try {
            this.type = factory.constructCollectionType(ArrayList.class, factory.constructParametricType(SimpleEntry.class, type.getKeyType(), type.getContentType()));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "", e);
        }
    }

    @Override
    public Map<K, V> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
            System.out.println("Deserialize :: " + " type " + type + " mapType " + mapType);
            if (type != null) {
                List<SimpleEntry<K, V>> listValues = ctxt.readValue(p, type);
                HashMap<K, V> value = new HashMap<>();
                for (SimpleEntry<K, V> e : listValues) {
                    System.out.println("key " + e.getKey() + " value " + e.getValue());
                    value.put(e.getKey(), e.getValue());
                }
                return value;
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "-->>>>", e);
        }
        return null;
    }


}