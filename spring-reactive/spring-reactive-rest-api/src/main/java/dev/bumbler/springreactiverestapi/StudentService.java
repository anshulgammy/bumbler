package dev.bumbler.springreactiverestapi;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentService {

  private static final List<Student> studentList = new ArrayList<>();

  private StudentRepository studentRepository;

  @Autowired
  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }


  /**
   * Utility to populate hard coded student list.
   */
  @PostConstruct
  private void loadStudents() {
    studentList.addAll(
        IntStream.rangeClosed(1, 20)
            .mapToObj(element -> {
              int rollNumber = element;
              String name = "Student " + rollNumber;
              return new Student(rollNumber, name);
            }).collect(Collectors.toList())
    );
  }

  /**
   * For hard coded rest api, blocking call.
   *
   * @return - List<Student>
   */
  public List<Student> getStudentsList() {
    List<Student> fetchedStudentList =
        IntStream.rangeClosed(1, 20)
            .peek(element -> System.out.println("Fetched student with id: " + element))
            .peek(StudentService::sleep)
            .mapToObj(element -> {
              int rollNumber = element;
              String name = "Student " + rollNumber;
              return new Student(rollNumber, name);
            }).collect(Collectors.toList());
    return fetchedStudentList;
  }

  private static void sleep(int element) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * For hard coded rest api, non blocking call.
   *
   * @return - Flux<Student>
   */
  public Flux<Student> getStudentsFlux() {
    Flux<Student> fetchedStudentFlux = Flux.range(1, studentList.size())
        .delayElements(Duration.ofSeconds(1))
        .doOnNext(element -> System.out.println("Fetched student with id: " + element))
        .map(index -> studentList.get(index - 1));
    return fetchedStudentFlux;
  }

  /**
   * Fetches students from database
   *
   * @return - Flux<Student>
   */
  public Flux<Student> getStudents() {
    return studentRepository.findAll();
  }

  public Mono<Student> getStudent(ServerRequest serverRequest) {
    long studentId = Long.valueOf(serverRequest.pathVariable("studentId"));
    return studentRepository.findById(studentId);
  }

  public Mono<Student> addStudent(ServerRequest serverRequest) {
    Mono<Student> newStudentMono = serverRequest.bodyToMono(Student.class);
    Mono<Student> addedStudentMono = newStudentMono.flatMap(studentRepository::insert);
    return addedStudentMono;
  }

  public Mono<Student> updateStudent(ServerRequest serverRequest) {
    Mono<Student> requestedStudent = serverRequest.bodyToMono(Student.class);
    AtomicLong studentId = new AtomicLong();
    requestedStudent.subscribe(student -> {
      studentId.set(student.getId());
      studentRepository.findById(student.getId())
          .map(s -> student)
          .flatMap(studentRepository::save);
    });

    return studentRepository.findById(studentId.get());
  }

  public Mono<Void> deleteStudent(ServerRequest serverRequest) {
    long studentId = Long.valueOf(serverRequest.pathVariable("studentId"));
    return studentRepository.deleteById(studentId);
  }
}
