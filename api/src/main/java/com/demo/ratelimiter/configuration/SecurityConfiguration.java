package com.demo.ratelimiter.configuration;

import com.demo.ratelimiter.configuration.properties.InMemoryAuthenticationProperties;
import com.demo.ratelimiter.security.UserRequestRateLimitingFilter;
import com.demo.ratelimiter.security.UsernameAuthenticationFilter;
import com.demo.ratelimiter.security.UsernameAuthenticationProvider;
import com.demo.ratelimiter.service.InMemoryUserDetailsService;
import com.demo.ratelimiter.service.UserRequestRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.jackson2.WebServletJackson2Module;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new CoreJackson2Module());
    mapper.registerModule(new WebServletJackson2Module());
    return mapper;
  }

  @Bean
  @Primary
  public RedisSerializer<String> springSessionDefaultRedisSerializer() {
    return new StringRedisSerializer();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      AuthenticationManager authenticationManager,
      UserRequestRateService userRequestRateService)
      throws Exception {
    return http.httpBasic()
        .disable()
        .csrf()
        .disable()
        .formLogin()
        .disable()
        .logout()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(
            new UsernameAuthenticationFilter(
                new OrRequestMatcher(new AntPathRequestMatcher("/endpoint/*")),
                authenticationManager),
            AnonymousAuthenticationFilter.class)
        .authorizeRequests()
        .anyRequest()
        .permitAll()
        .and()
        .build();
  }

  @Bean
  public FilterRegistrationBean<UserRequestRateLimitingFilter> userRequestRateFilter(
      UserRequestRateService userRequestRateService) {
    FilterRegistrationBean<UserRequestRateLimitingFilter> registration =
        new FilterRegistrationBean<>();
    registration.setFilter(new UserRequestRateLimitingFilter(userRequestRateService));
    registration.addUrlPatterns("/endpoint/*");
    return registration;
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().antMatchers("/static/**");
  }

  @Bean
  public InMemoryUserDetailsService inMemoryUserDetailsService(
      InMemoryAuthenticationProperties properties, BCryptPasswordEncoder passwordEncoder) {
    return new InMemoryUserDetailsService(properties.getUsers(), passwordEncoder);
  }

  @Bean
  public AuthenticationManager authenticationManager(
      HttpSecurity http, InMemoryUserDetailsService userDetailsService) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(new UsernameAuthenticationProvider(userDetailsService))
        .build();
  }
}
