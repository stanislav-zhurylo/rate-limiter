package com.demo.ratelimiter.configuration;

import com.demo.ratelimiter.configuration.properties.RedisConnectionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfiguration {

  @Bean
  public JedisConnectionFactory connectionFactory(RedisConnectionProperties connectionProperties) {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setHostName(connectionProperties.getHost());
    configuration.setPort(connectionProperties.getPort());
    configuration.setPassword(connectionProperties.getPassword());
    return new JedisConnectionFactory(configuration);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory) {
    RedisSerializer<Object> jsonSerializer = RedisSerializer.json();
    RedisSerializer<String> stringSerializer = RedisSerializer.string();
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(stringSerializer);
    template.setHashKeySerializer(stringSerializer);
    template.setValueSerializer(jsonSerializer);
    template.setStringSerializer(stringSerializer);
    template.setHashValueSerializer(jsonSerializer);
    template.afterPropertiesSet();
    return template;
  }
}
