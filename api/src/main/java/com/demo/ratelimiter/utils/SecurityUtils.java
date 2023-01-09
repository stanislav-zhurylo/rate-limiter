package com.demo.ratelimiter.utils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

  public static Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public static String getUsername() {
    Authentication authentication = getAuthentication();
    return authentication != null ? authentication.getName() : null;
  }

  public static boolean isAnonymousUser() {
    return getAuthentication() instanceof AnonymousAuthenticationToken;
  }

  public static String getUserReference(HttpServletRequest request) {
    return isAnonymousUser() ? request.getRemoteAddr() : getUsername();
  }
}
