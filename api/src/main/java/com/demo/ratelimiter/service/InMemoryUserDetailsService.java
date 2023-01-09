package com.demo.ratelimiter.service;

import com.demo.ratelimiter.configuration.properties.InMemoryAuthenticationProperties.InMemoryUserDetails;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class InMemoryUserDetailsService implements UserDetailsService {

  private final Map<String, User> userRepository;

  public InMemoryUserDetailsService(
      List<InMemoryUserDetails> users, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository =
        users.stream()
            .map(
                user ->
                    (User)
                        User.builder()
                            .username(user.getUsername())
                            .authorities(user.getRoles())
                            .password(passwordEncoder.encode(user.getPassword()))
                            .accountLocked(Boolean.TRUE.equals(user.getLocked()))
                            .build())
            .collect(Collectors.toMap(User::getUsername, Function.identity()));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.get(username);
  }
}
