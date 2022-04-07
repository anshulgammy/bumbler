package nerd.utopian.completablefuture.springboot;

import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

  private EmployeeService employeeService;

  @Autowired
  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping("/employee/{id}")
  public ResponseEntity<Employee> getEmployeeById(@PathVariable long id)
      throws ExecutionException, InterruptedException {

    return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeeById(id).get());
  }

  @GetMapping("/employee")
  public ResponseEntity<List<Employee>> getAllEmployees()
      throws ExecutionException, InterruptedException {

    return ResponseEntity.status(HttpStatus.OK).body(employeeService.getAllEmployees().get());
  }

}
