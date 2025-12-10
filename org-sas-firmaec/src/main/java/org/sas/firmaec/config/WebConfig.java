package org.sas.firmaec.config;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void configureMessageConverters(List converters) {
        super.configureMessageConverters(converters);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new HibernateAwareObjectMapper());
        converters.add(converter);
    }

    private class HibernateAwareObjectMapper extends ObjectMapper {
        public HibernateAwareObjectMapper() {
            this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            configure(SerializationFeature.INDENT_OUTPUT, true);
            this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            this.addHandler(new DeserializationProblemHandler() {
                @Override
                public Object handleWeirdStringValue(DeserializationContext ctxt, Class<?> targetType, String valueToConvert, String failureMsg) throws IOException {
                    if (targetType == Boolean.class) {
                        System.out.println("targetType " + targetType + " valueToConvert " + valueToConvert);
                        return Boolean.TRUE.toString().equalsIgnoreCase(valueToConvert);
                    }
                    return super.handleWeirdStringValue(ctxt, targetType, valueToConvert, failureMsg);
                }
            });
        }

    }
}
