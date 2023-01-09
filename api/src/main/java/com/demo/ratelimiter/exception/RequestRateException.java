package com.demo.ratelimiter.exception;

@SuppressWarnings("serial")
public class RequestRateException extends RuntimeException {

  public RequestRateException(String message) {
    super(message);
  }
}
