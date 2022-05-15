package nerd.utopian.initiator.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InitiatorService {

  private RestTemplate restTemplate;

  public InitiatorService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String getData(int id) {
    return restTemplate.getForObject("http://localhost:9092/v1/api/orchestrator/data/" + id,
        String.class);
  }
}
