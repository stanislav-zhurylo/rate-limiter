package com.demo.ratelimiter;

import com.demo.ratelimiter.configuration.properties.InMemoryAuthenticationProperties;
import com.demo.ratelimiter.configuration.properties.RedisConnectionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@ComponentScan({
  "com.demo.ratelimiter.configuration",
  "com.demo.ratelimiter.controller",
  "com.demo.ratelimiter.service"
})
@EnableAutoConfiguration(exclude = {SessionAutoConfiguration.class})
@EnableRedisRepositories(
    basePackages = {"com.demo.ratelimiter.repository"},
    enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
@EnableConfigurationProperties({
  RedisConnectionProperties.class,
  InMemoryAuthenticationProperties.class
})
public class RateLimiterApplication {

  public static void main(String[] args) {
    SpringApplication.run(RateLimiterApplication.class, args);
  }
}
