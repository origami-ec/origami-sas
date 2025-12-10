package org.ibarra.conf;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.type.MapType;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MapDeserializerModifier extends BeanDeserializerModifier {

    @Override
    @SuppressWarnings("rawtypes")
    public JsonDeserializer<?> modifyMapDeserializer(DeserializationConfig config, MapType type,
                                                     BeanDescription beanDesc, JsonDeserializer<?> deserializer) {

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(type.getKeyType().getRawClass());

        if (keyDeserializer == null)
            return new MapSimpleDeserializer(type, config.getTypeFactory());

        return deserializer;
    }

}