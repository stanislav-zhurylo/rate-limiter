package com.demo.ratelimiter.entity;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Data
@Builder
@AllArgsConstructor
@RedisHash(value = "ratelimiter::rates::UserRequestRate")
public class UserRequestRate {

  @Id @Indexed private String reference;

  private int score;

  private boolean isBanned;

  private LocalDateTime firstRequestUtc;

  @TimeToLive(unit = TimeUnit.MILLISECONDS)
  private long timeToLive;
}
