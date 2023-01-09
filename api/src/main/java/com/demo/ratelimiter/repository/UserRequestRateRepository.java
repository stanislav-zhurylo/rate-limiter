package com.demo.ratelimiter.repository;

import com.demo.ratelimiter.entity.UserRequestRate;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRequestRateRepository extends CrudRepository<UserRequestRate, String> {

  Optional<UserRequestRate> findFirstByReference(String userReference);
}
