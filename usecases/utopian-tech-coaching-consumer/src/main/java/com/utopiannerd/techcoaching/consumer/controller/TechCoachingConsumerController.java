package com.utopiannerd.techcoaching.consumer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
public class TechCoachingConsumerController {

  private WebClient webClient;

  public TechCoachingConsumerController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/students/all")
  public Flux getAllStudents() {
    return webClient.get().uri("/students/all").retrieve().bodyToFlux(Object.class);
  }

  @GetMapping("/batch/available")
  public Flux getAvailableBatches(
      @RequestParam String batchStartDate,
      @RequestParam String batchEndDate,
      @RequestParam String totalSeatsAvailable) {
    return webClient
        .get()
        .uri(
            "/batch/available?batchStartDate="
                + batchStartDate
                + "&batchEndDate="
                + batchEndDate
                + "&totalSeatsAvailable="
                + totalSeatsAvailable)
        .retrieve()
        .bodyToFlux(Object.class);
  }
}
