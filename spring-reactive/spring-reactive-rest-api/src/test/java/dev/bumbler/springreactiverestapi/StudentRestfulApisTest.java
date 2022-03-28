package dev.bumbler.springreactiverestapi;


import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@WebFluxTest(StudentRouter.class)
@ComponentScan(basePackages = {"dev.bumbler.springreactiverestapi"})
public class StudentRestfulApisTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private StudentRepository studentRepository;

  @Test
  public void testGetStudents() {
    Student student_1 = new Student(1, "Student 1", 101);
    Student student_2 = new Student(2, "Student 2", 102);

    Flux<Student> studentFlux = Flux.just(student_1, student_2);

    when(studentRepository.findAll()).thenReturn(studentFlux);

    Flux<Student> responseBody = webTestClient.get()
        .uri("/v1/api/router/students")
        .exchange()
        .expectStatus()
        .isOk()
        .returnResult(Student.class)
        .getResponseBody();

    StepVerifier.create(responseBody)
        .expectSubscription()
        .expectNext(student_1)
        .expectNext(student_2)
        .verifyComplete();
  }

  @Test
  public void testGetStudentById() {
    Student student_1 = new Student(1, "Student 1", 101);

    Mono<Student> studentMono = Mono.just(student_1);

    when(studentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(studentMono);

    Flux<Student> responseBody = webTestClient.get()
        .uri("/v1/api/router/student/1")
        .exchange()
        .expectStatus()
        .isOk()
        .returnResult(Student.class)
        .getResponseBody();

    StepVerifier.create(responseBody)
        .expectSubscription()
        .expectNext(student_1)
        .verifyComplete();
  }

  @Test
  public void testAddStudent() {
    Student student_1 = new Student(1, "Student 1", 101);

    Mono<Student> studentMono = Mono.just(student_1);

    when(studentRepository.insert(student_1)).thenReturn(studentMono);

    Flux<Student> responseBody = webTestClient.post()
        .uri("/v1/api/router/student/add")
        .accept(MediaType.TEXT_EVENT_STREAM)
        .body(studentMono, Student.class)
        .exchange()
        .expectStatus()
        .isOk()
        .returnResult(Student.class)
        .getResponseBody();

    StepVerifier.create(responseBody)
        .expectSubscription()
        .expectNext(student_1)
        .verifyComplete();
  }

  @Test
  public void testUpdateStudent() {
    Student student_1 = new Student(1, "Student 1", 101);

    Mono<Student> studentMono = Mono.just(student_1);

    when(studentRepository.save(student_1)).thenReturn(studentMono);

    Flux<Student> responseBody = webTestClient.put()
        .uri("/v1/api/router/student/update")
        .accept(MediaType.TEXT_EVENT_STREAM)
        .body(studentMono, Student.class)
        .exchange()
        .expectStatus()
        .isOk()
        .returnResult(Student.class)
        .getResponseBody();

    StepVerifier.create(responseBody)
        .expectSubscription()
        .expectNext(student_1)
        .verifyComplete();
  }

  @Test
  public void testDeleteStudent() {
    when(studentRepository.deleteById(ArgumentMatchers.anyLong())).thenReturn(Mono.empty());

    webTestClient.delete()
        .uri("/v1/api/router/student/delete/1")
        .exchange()
        .expectStatus()
        .isOk()
        .returnResult(Void.class)
        .getResponseBody();
  }

}
