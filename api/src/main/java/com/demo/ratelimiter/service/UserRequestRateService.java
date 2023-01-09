package com.demo.ratelimiter.service;

import static com.demo.ratelimiter.utils.DateTimeUtils.now;

import com.demo.ratelimiter.entity.UserRequestRate;
import com.demo.ratelimiter.repository.UserRequestRateRepository;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRequestRateService {
  public static final Integer USER_BAN_MINUTES = 60;

  private final UserRequestRateRepository repository;

  public UserRequestRate rememberRequest(
      String userReference, int initialScore, int subtractScore) {
    UserRequestRate requestRate =
        repository
            .findFirstByReference(userReference)
            .orElseGet(() -> repository.save(assembleUserRequestRate(userReference, initialScore)));
    int score = Math.max(-1, requestRate.getScore() - subtractScore);
    requestRate.setScore(score);
    requestRate.setBanned(score < 0);
    requestRate.setTimeToLive(calculateTimeToLive(requestRate));
    return repository.save(requestRate);
  }

  private long calculateTimeToLive(UserRequestRate requestRate) {
    var futureMilliseconds =
        requestRate
            .getFirstRequestUtc()
            .plusMinutes(USER_BAN_MINUTES)
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli();
    var nowMilliseconds = now(ZoneOffset.UTC).toInstant(ZoneOffset.UTC).toEpochMilli();
    return Math.max(futureMilliseconds - nowMilliseconds, -1L);
  }

  private UserRequestRate assembleUserRequestRate(String userReference, int initialScore) {
    return UserRequestRate.builder()
        .reference(userReference)
        .score(initialScore)
        .firstRequestUtc(now(ZoneOffset.UTC))
        .timeToLive(
            now(ZoneOffset.UTC)
                .plusMinutes(USER_BAN_MINUTES)
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli())
        .isBanned(false)
        .build();
  }
}
