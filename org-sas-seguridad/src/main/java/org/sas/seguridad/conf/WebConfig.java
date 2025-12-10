package org.sas.seguridad.conf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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
            Hibernate5Module hm = new Hibernate5Module();
            hm.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
            hm.configure(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION, false);
            registerModule(hm);
            configure(SerializationFeature.INDENT_OUTPUT, true);
            this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }

    }
}
