package com.demo.ratelimiter.security;

import static com.demo.ratelimiter.utils.SecurityUtils.getUserReference;
import static com.demo.ratelimiter.utils.SecurityUtils.isAnonymousUser;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

import com.demo.ratelimiter.entity.UserRequestRate;
import com.demo.ratelimiter.exception.RequestRateException;
import com.demo.ratelimiter.service.UserRequestRateService;
import com.demo.ratelimiter.utils.Locations;
import java.io.IOException;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.PathContainer;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;
import org.springframework.web.util.pattern.PathPatternParser;

@RequiredArgsConstructor
public class UserRequestRateLimitingFilter extends OncePerRequestFilter {

  private static final PathPattern PATH_PATTERN = new PathPatternParser().parse(Locations.ENDPOINT);
  public static final Integer AUTHENTICATED_USER_INITIAL_SCORE = 1000;
  public static final Integer ANONYMOUS_USER_INITIAL_SCORE = 500;
  private final UserRequestRateService service;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    UserRequestRate requestRate =
        service.rememberRequest(
            getUserReference(request),
            isAnonymousUser() ? ANONYMOUS_USER_INITIAL_SCORE : AUTHENTICATED_USER_INITIAL_SCORE,
            getRequestCost(request));
    if (requestRate.isBanned()) {
      throw new RequestRateException("Access denied due to request rate limit");
    }
    super.doFilter(request, response, filterChain);
  }

  private int getRequestCost(HttpServletRequest request) {
    return firstNonNull(getIntPathVariable(request, "number"), 0) * 100;
  }

  private Integer getIntPathVariable(HttpServletRequest request, String key) {
    String variable = getUriVariables(request).get(key);
    return variable != null ? Integer.valueOf(variable) : null;
  }

  private Map<String, String> getUriVariables(HttpServletRequest request) {
    PathMatchInfo matchedResult =
        PATH_PATTERN.matchAndExtract(PathContainer.parsePath(request.getServletPath()));
    return matchedResult != null ? matchedResult.getUriVariables() : Map.of();
  }
}
