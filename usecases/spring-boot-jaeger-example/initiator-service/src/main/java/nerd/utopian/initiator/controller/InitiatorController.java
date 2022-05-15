package nerd.utopian.initiator.controller;

import nerd.utopian.initiator.service.InitiatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/initiator")
public class InitiatorController {

  private InitiatorService initiatorService;

  public InitiatorController(
      InitiatorService initiatorService) {
    this.initiatorService = initiatorService;
  }

  @GetMapping("/data/{id}")
  public ResponseEntity<String> getData(@PathVariable("id") int id) {
    return ResponseEntity.ok(initiatorService.getData(id));
  }
}
