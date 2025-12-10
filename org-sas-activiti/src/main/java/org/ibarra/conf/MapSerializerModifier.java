package org.ibarra.conf;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import com.fasterxml.jackson.databind.type.MapType;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class MapSerializerModifier extends BeanSerializerModifier {

    @Override
    @SuppressWarnings("rawtypes")
    public JsonSerializer<?> modifyMapSerializer(SerializationConfig config, MapType valueType,
                                                 BeanDescription beanDesc, JsonSerializer<?> serializer) {
//        System.out.println("MapSerializerModifier:: " + valueType + " keyType: " + valueType.getRawClass());
        JsonSerializer keySerializer = StdKeySerializers.getStdKeySerializer(config,
                valueType.getKeyType().getRawClass(), false);
        System.out.println("MapSerializerModifier keySerializer :: " + keySerializer + " valueType.getRawClass() " +valueType.getRawClass());
        if (valueType.getRawClass().equals(HashMap.class) || valueType.getRawClass().equals(Map.class) || valueType.getRawClass().equals(LinkedHashMap.class))
            return new MapSimpleSerializer();

        return serializer;
    }


}
