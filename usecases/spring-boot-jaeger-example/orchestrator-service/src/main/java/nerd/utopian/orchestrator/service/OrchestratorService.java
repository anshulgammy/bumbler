package nerd.utopian.orchestrator.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrchestratorService {

  private RestTemplate restTemplate;

  public OrchestratorService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String getData(int id) {
    return restTemplate.getForObject("http://numbersapi.com/" + id, String.class);
  }
}
