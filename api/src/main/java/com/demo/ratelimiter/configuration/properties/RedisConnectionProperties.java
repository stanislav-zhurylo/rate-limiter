package com.demo.ratelimiter.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConnectionProperties {
  private String host;
  private String password;
  private Integer port;
}
