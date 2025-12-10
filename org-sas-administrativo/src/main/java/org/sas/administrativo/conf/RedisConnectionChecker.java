package org.sas.administrativo.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class RedisConnectionChecker {

    private final RedisConnectionFactory redisConnectionFactory;
    private static final Logger logger = Logger.getLogger(RedisConnectionChecker.class.getName());

    @Autowired
    public RedisConnectionChecker(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    public boolean isRedisAvailable() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            return connection.ping() != null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error verificando conexión Redis: " + e.getMessage(), e);
            return false;
        }
    }
}

