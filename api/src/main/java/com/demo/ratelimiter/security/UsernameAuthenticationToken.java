package com.demo.ratelimiter.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class UsernameAuthenticationToken extends UsernamePasswordAuthenticationToken {

  public UsernameAuthenticationToken(
      Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
  }

  public static UsernameAuthenticationToken ofAuthenticated(UserDetails userDetails) {
    return new UsernameAuthenticationToken(
        userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
  }

  public static AnonymousAuthenticationToken ofAnonymous(String username) {
    return new AnonymousAuthenticationToken(
        "anonymousUserKey", username, List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
  }
}
