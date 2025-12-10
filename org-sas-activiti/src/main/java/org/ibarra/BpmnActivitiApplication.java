package org.ibarra;


import org.activiti.core.common.spring.security.config.ActivitiSpringSecurityAutoConfiguration;
import org.activiti.core.common.spring.security.policies.config.ActivitiSpringSecurityPoliciesAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class

        , ActivitiSpringSecurityAutoConfiguration.class, ActivitiSpringSecurityPoliciesAutoConfiguration.class})
@EnableAsync
@EnableScheduling
@EnableJpaRepositories(basePackages = "org.ibarra.repository")
@EntityScan(basePackages = "org.ibarra.entity")
public class BpmnActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BpmnActivitiApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    public HttpMessageConverters customConverters() {
        ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        return new HttpMessageConverters(arrayHttpMessageConverter);
    }


}
