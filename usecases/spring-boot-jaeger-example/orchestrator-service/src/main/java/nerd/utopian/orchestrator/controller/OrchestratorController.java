package nerd.utopian.orchestrator.controller;

import nerd.utopian.orchestrator.service.OrchestratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/orchestrator")
public class OrchestratorController {

  private OrchestratorService orchestratorService;

  public OrchestratorController(
      OrchestratorService orchestratorService) {
    this.orchestratorService = orchestratorService;
  }

  @GetMapping("/data/{id}")
  public ResponseEntity<String> getData(@PathVariable("id") int id) {
    return ResponseEntity.ok(orchestratorService.getData(id));
  }
}
