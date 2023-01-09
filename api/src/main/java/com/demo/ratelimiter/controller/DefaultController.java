package com.demo.ratelimiter.controller;

import com.demo.ratelimiter.utils.Locations;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@NoArgsConstructor
public class DefaultController {

  @GetMapping(Locations.INDEX)
  public String indexPage() {
    return "index";
  }

  @GetMapping(Locations.AUTH)
  public String loginPage() {
    return "login";
  }

  @GetMapping(Locations.ENDPOINT)
  public ResponseEntity<String> endpointPage(@PathVariable int number) {
    return ResponseEntity.ok("Your request has been processed successfully");
  }
}
