package com.demo.ratelimiter.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateTimeUtils {
  public static LocalDateTime now(ZoneOffset offset) {
    return Instant.now().atZone(offset).toLocalDateTime();
  }
}
