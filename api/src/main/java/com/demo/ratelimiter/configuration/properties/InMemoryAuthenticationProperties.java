package com.demo.ratelimiter.configuration.properties;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "in-memory-authentication")
public class InMemoryAuthenticationProperties {
  private Boolean enabled;
  private List<InMemoryUserDetails> users;

  @Data
  public static final class InMemoryUserDetails {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String[] roles;
    private Boolean locked;
  }
}
