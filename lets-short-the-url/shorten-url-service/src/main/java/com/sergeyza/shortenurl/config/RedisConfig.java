package com.sergeyza.shortenurl.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sergeyza.shortenurl.persistence.entity.UrlMapEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    private final RedisPropertiesConfig redisProps;

    public RedisConfig(RedisPropertiesConfig redisProps) {
        this.redisProps = redisProps;
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        System.out.println("Connecting to Redis at " + redisProps.getHost() + ":" + redisProps.getPort());
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisProps.getHost(), redisProps.getPort()));
    }

    @Bean
    public RedisTemplate<String, UrlMapEntity> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, UrlMapEntity> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        // Custom ObjectMapper with JavaTimeModule
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Jackson2JsonRedisSerializer<UrlMapEntity> serializer = new Jackson2JsonRedisSerializer<>(UrlMapEntity.class);
        serializer.setObjectMapper(mapper); //still works
        template.setValueSerializer(serializer);
        return template;
    }

}
