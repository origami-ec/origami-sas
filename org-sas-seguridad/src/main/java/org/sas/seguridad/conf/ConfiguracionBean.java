package org.sas.seguridad.conf;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.sas.seguridad.util.Constantes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.Arrays;

@Configuration
public class ConfiguracionBean {

    @Value("${spring.redis.host}")
    private String hostName;
    @Value("${spring.redis.port}")
    private int port = 6379;

    @Bean
    public Module hibernateModule() {
        return new Hibernate5Module();
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(hostName);
        configuration.setPort(port);
        return new LettuceConnectionFactory(configuration);
    }


    @Bean
    @Primary
    public CacheManager cacheManager() {
        try {
            RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory())
                    .cacheDefaults(defaultConfig())
                    .build();
            redisCacheManager.initializeCaches();
            return redisCacheManager;
        } catch (Exception e) {
            // Si la chache del redis no esta disponible
            SimpleCacheManager localCacheManager = new SimpleCacheManager();
            localCacheManager.setCaches(Arrays.asList(
                    new ConcurrentMapCache(Constantes.cacheMenus),
                    new ConcurrentMapCache(Constantes.cacheMenusXusuario)
            ));
            return localCacheManager;
        }
    }

    private RedisCacheConfiguration defaultConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10));
    }


}
