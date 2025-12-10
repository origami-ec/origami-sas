package org.sas.zull.conf;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.logging.Logger;

@Configuration
@EnableCaching
public class CachingConfig {

    private Logger logger = Logger.getLogger(CachingConfig.class.getName());
    @Bean
    public CacheManager evictAllCaches() {
        return new ConcurrentMapCacheManager("usuarios");
    }
    @Scheduled(fixedRate = 6000)
    public void evictAllcachesAtIntervals() {
        logger.info("evictAllcachesAtIntervals");
        evictAllCaches();
    }
}
