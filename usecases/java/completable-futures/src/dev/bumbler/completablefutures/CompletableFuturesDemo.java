package dev.bumbler.completablefutures;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CompletableFuturesDemo {

  public static void main(String[] args) {

    EmployeesService employeesService = new EmployeesService();

    // Created ExecutorService of my own, so that CompletableFuture don't make use of common fork
    // join pool.

    // If I supply my own custom ExecutorService, CompletableFuture will make use of threads from
    // my ExecutorService's thread pool.
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    // Every .thenApply or .thenAccept or .thenRun used below are being run on a separate thread.
    // (I was trying to be over smart)

    // This could be easily achieved without this complicated code below by using .thenApplyAsync,
    // .thenAcceptAsync, thenRunAsync, with straight forward and concise code.

    // Different CompletableFuture are being used below, for their intended responsibility.
    // This is what we sometimes call as 'CompletableFuture Chaining'. Such a chaining is not
    // available with Future objects.
    try {
      // Fetching all the employees present in the company's database.
      // (Tried to simulate such a situation, there is no DB though in this example)
      employeesService
          .getAllEmployeesCompletableFuture(executorService)
          .thenApply(
              // Fetching those employees who are new joiners,
              // whose System Current Date - DateOfJoining <= 6 months
              employees -> {
                try {
                  return employeesService.getNewJoinerEmployees(employees, executorService);
                } catch (ExecutionException ex) {
                  ex.printStackTrace();
                  return null;
                } catch (InterruptedException ex) {
                  ex.printStackTrace();
                  return null;
                }
              })
          .thenApply(
              // Fetching those employees who are new joiners,
              // and whose trainings are not yet completed.
              employees -> {
                try {
                  return employeesService.getTrainingsNotCompletedEmployees(employees.get(),
                      executorService);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                  return null;
                } catch (ExecutionException e) {
                  e.printStackTrace();
                  return null;
                }
              })
          .thenAccept(
              // Collecting List<String> emailIds of those new joiner employees who have not
              // completed their trainings, and sending reminder email on those email ids.
              employees -> {
            try {
              employeesService.sendEmailsToEmployeesForTraining(employees.get(),
                  executorService);
            } catch (InterruptedException e) {
              e.printStackTrace();
            } catch (ExecutionException e) {
              e.printStackTrace();
            }
          });
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
  }
}

class EmployeesService {

  CompletableFuture<List<Employee>> getAllEmployeesCompletableFuture(
      ExecutorService executorService) throws ExecutionException, InterruptedException {
    return CompletableFuture.supplyAsync(() -> {
          System.out.println(
              "getAllEmployeesCompletableFuture on thread: " + Thread.currentThread().getName());
          List<Employee> employeesList = EmployeesRepository.instance().fetchAllEmployees();
          System.out.println(employeesList);
          return employeesList;
        },
        executorService);
  }

  CompletableFuture<List<Employee>> getNewJoinerEmployees(List<Employee> employees,
      ExecutorService executorService)
      throws ExecutionException, InterruptedException {
    return CompletableFuture.supplyAsync(() -> {
      System.out.println("getNewJoinerEmployees on thread: " + Thread.currentThread().getName());
      List<Employee> employeesList = employees.stream().filter(
              employee -> ChronoUnit.MONTHS.between(employee.getDateOfJoining(), LocalDate.now()) <= 6)
          .collect(Collectors.toList());
      System.out.println(employeesList);
      return employeesList;
    }, executorService);
  }

  CompletableFuture<List<Employee>> getTrainingsNotCompletedEmployees(List<Employee> employees,
      ExecutorService executorService) {
    return CompletableFuture.supplyAsync(() -> {
      System.out.println(
          "getTrainingsNotCompletedEmployees on thread: " + Thread.currentThread().getName());
      List<Employee> employeesList = employees.stream()
          .filter(employee -> !employee.isTrainingsCompleted())
          .collect(Collectors.toList());
      System.out.println(employeesList);
      return employeesList;
    }, executorService);
  }

  void sendEmailsToEmployeesForTraining(List<Employee> employees, ExecutorService executorService) {
    CompletableFuture.runAsync(() -> {
      System.out.println(
          "sendEmailsToEmployeesForTraining on thread: " + Thread.currentThread().getName());
      employees.stream().map(employee -> employee.getEmailId())
          .forEach(emailId -> System.out.println("Sending email to: " + emailId));
    }, executorService);
  }
}

class EmployeesRepository {

  private static final List<Employee> EMPLOYEE_LIST = new ArrayList<>();
  private static EmployeesRepository employeesRepositoryInstance = null;

  private EmployeesRepository() {
    EMPLOYEE_LIST.addAll(Arrays.asList(
        new Employee(1001, "Employee 1", "Manager", "employee1@abc.com", LocalDate.of(2020, 04, 02),
            true),
        new Employee(1002, "Employee 2", "Manager", "employee2@abc.com", LocalDate.of(1999, 04, 02),
            true),
        new Employee(1003, "Employee 3", "Manager", "employee3@abc.com", LocalDate.of(2019, 04, 02),
            true),
        new Employee(1004, "Employee 4", "Manager", "employee4@abc.com", LocalDate.of(2022, 02, 02),
            false),
        new Employee(1005, "Employee 5", "Manager", "employee5@abc.com", LocalDate.of(2020, 04, 02),
            true),
        new Employee(1006, "Employee 6", "Manager", "employee6@abc.com", LocalDate.of(1997, 04, 02),
            true),
        new Employee(1007, "Employee 7", "Manager", "employee7@abc.com", LocalDate.of(2020, 04, 02),
            true),
        new Employee(1008, "Employee 8", "Manager", "employee8@abc.com", LocalDate.of(2020, 04, 02),
            false),
        new Employee(1009, "Employee 9", "Manager", "employee9@abc.com", LocalDate.of(2022, 01, 02),
            false), new Employee(10010, "Employee 10", "Manager", "employee10@abc.com",
            LocalDate.of(2020, 04, 02), true)));
  }

  static EmployeesRepository instance() {
    if (employeesRepositoryInstance == null) {
      synchronized (EmployeesRepository.class) {
        if (employeesRepositoryInstance == null) {
          employeesRepositoryInstance = new EmployeesRepository();
        }
      }
    }
    return employeesRepositoryInstance;
  }

  List<Employee> fetchAllEmployees() {
    return EMPLOYEE_LIST;
  }
}

class Employee {

  private int id;
  private String fullName;
  private String designation;
  private String emailId;
  private LocalDate dateOfJoining;
  private boolean trainingsCompleted;

  public Employee(int id, String fullName, String designation, String emailId,
      LocalDate dateOfJoining, boolean trainingsCompleted) {
    this.id = id;
    this.fullName = fullName;
    this.designation = designation;
    this.emailId = emailId;
    this.dateOfJoining = dateOfJoining;
    this.trainingsCompleted = trainingsCompleted;
  }

  public int getId() {
    return id;
  }

  public String getFullName() {
    return fullName;
  }

  public String getDesignation() {
    return designation;
  }

  public String getEmailId() {
    return emailId;
  }

  public LocalDate getDateOfJoining() {
    return dateOfJoining;
  }

  public boolean isTrainingsCompleted() {
    return trainingsCompleted;
  }

  @Override
  public String toString() {
    return "Employee{" +
        "id=" + id +
        ", fullName='" + fullName + '\'' +
        ", designation='" + designation + '\'' +
        ", emailId='" + emailId + '\'' +
        ", dateOfJoining=" + dateOfJoining +
        ", trainingsCompleted=" + trainingsCompleted +
        '}';
  }
}
