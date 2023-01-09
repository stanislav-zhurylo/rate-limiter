package com.demo.ratelimiter.service;

import static com.demo.ratelimiter.service.UserRequestRateService.USER_BAN_MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.demo.ratelimiter.entity.UserRequestRate;
import com.demo.ratelimiter.repository.UserRequestRateRepository;
import com.demo.ratelimiter.utils.DateTimeUtils;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserRequestRateServiceTest {

  @Mock private UserRequestRateRepository repository;
  @InjectMocks private UserRequestRateService service;

  @Captor private ArgumentCaptor<UserRequestRate> userRequestRateArgumentCaptor;

  @Test
  void rememberRequest_shouldCreateRequestRate() {
    String userReference = "USER_REFERENCE";
    UserRequestRate userRequestRate =
        UserRequestRate.builder()
            .reference(userReference)
            .score(1000)
            .isBanned(false)
            .firstRequestUtc(DateTimeUtils.now(ZoneOffset.UTC))
            .timeToLive(USER_BAN_MINUTES)
            .build();

    doReturn(Optional.empty()).when(repository).findFirstByReference(userReference);
    doReturn(userRequestRate).when(repository).save(any());

    service.rememberRequest(userReference, 1000, 1001);

    verify(repository, times(2)).save(userRequestRateArgumentCaptor.capture());
    List<UserRequestRate> result = userRequestRateArgumentCaptor.getAllValues();
    assertThat(result.get(0).getReference()).isEqualTo("USER_REFERENCE");
    assertThat(result.get(0).getScore()).isEqualTo(1000);
    assertThat(result.get(0).isBanned()).isFalse();
    assertThat(result.get(1).getReference()).isEqualTo("USER_REFERENCE");
    assertThat(result.get(1).getScore()).isEqualTo(-1);
    assertThat(result.get(1).isBanned()).isTrue();
  }

  @Test
  void rememberRequest_shouldUpdateRequestRate() {
    String userReference = "USER_REFERENCE";
    UserRequestRate userRequestRate =
        UserRequestRate.builder()
            .reference(userReference)
            .score(1000)
            .isBanned(false)
            .firstRequestUtc(DateTimeUtils.now(ZoneOffset.UTC))
            .timeToLive(USER_BAN_MINUTES)
            .build();

    doReturn(Optional.of(userRequestRate)).when(repository).findFirstByReference(userReference);

    service.rememberRequest(userReference, 1000, 200);

    verify(repository).save(userRequestRateArgumentCaptor.capture());
    UserRequestRate result = userRequestRateArgumentCaptor.getValue();
    assertThat(result.getReference()).isEqualTo("USER_REFERENCE");
    assertThat(result.getScore()).isEqualTo(800);
    assertThat(result.isBanned()).isFalse();
  }
}
