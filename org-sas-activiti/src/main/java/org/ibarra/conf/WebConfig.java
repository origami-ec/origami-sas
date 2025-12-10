package org.ibarra.conf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
    }

    public MappingJackson2HttpMessageConverter messageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        jsonConverter.setObjectMapper(objectMapper);

        return jsonConverter;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());
    }

    @Override
    public void configureMessageConverters(List converters) {

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new HibernateAwareObjectMapper());
        converters.add(converter);
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    }

    private class HibernateAwareObjectMapper extends ObjectMapper {

        ObjectMapper om = null;

        public HibernateAwareObjectMapper() {
            this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Hibernate6Module hm = new Hibernate6Module();
            hm.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, true);
//            hm.configure(Hibernate6Module.Feature.USE_TRANSIENT_ANNOTATION, false);
            hm.disable(Hibernate6Module.Feature.USE_TRANSIENT_ANNOTATION);
            hm.configure(Hibernate6Module.Feature.REPLACE_PERSISTENT_COLLECTIONS, false);
            registerModule(hm);
            configure(SerializationFeature.INDENT_OUTPUT, true);
            this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            om = this;
            this.addHandler(new DeserializationProblemHandler() {
                @Override
                public Object handleWeirdStringValue(DeserializationContext ctxt, Class<?> targetType, String valueToConvert, String failureMsg) throws IOException {
                    if (targetType == Boolean.class) {
                        return Boolean.TRUE.toString().equalsIgnoreCase(valueToConvert);
                    }
                    return super.handleWeirdStringValue(ctxt, targetType, valueToConvert, failureMsg);
                }
            });

        }

    }


}
