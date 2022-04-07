package nerd.utopian.completablefuture.springboot;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

  private static final List<Employee> EMPLOYEES_LIST = Arrays.asList(
      new Employee(1001, "Rakesh Kumar"),
      new Employee(1002, "Suresh Kumar"),
      new Employee(1003, "Jignesh Kumar"));

  @Async("customThreadPoolTaskExecutor")
  public CompletableFuture<List<Employee>> getAllEmployees() {

    LOGGER.info("EmployeeService.getAllEmployees() running on: {}",
        Thread.currentThread().getName());

    return CompletableFuture.completedFuture(EMPLOYEES_LIST);
  }

  @Async("customThreadPoolTaskExecutor")
  public CompletableFuture<Employee> getEmployeeById(long id) {

    LOGGER.info("EmployeeService.getEmployeeById() running on: {}",
        Thread.currentThread().getName());

    Optional<Employee> employee = EMPLOYEES_LIST.stream().filter(emp -> emp.getId() == id)
        .findFirst();

    return CompletableFuture.completedFuture(employee.isPresent() ? employee.get() : null);
  }
}
