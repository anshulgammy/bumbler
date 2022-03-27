package dev.bumbler.springreactiverestapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class StudentController {

  private StudentService studentService;

  @Autowired
  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  /**
   * Fetches List of Students synchronously.
   *
   * @return - ResponseEntity<List<Student>>
   */
  @GetMapping("/students")
  public ResponseEntity<List<Student>> getSyncStudents() {
    return ResponseEntity.ok(studentService.getStudentsList());
  }

  /**
   * Fetches Flux of Students.
   *
   * @return - Flux<Student>
   */
  @GetMapping(value = "/async/students", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Student> getAsyncStudents() {
    return studentService.getStudentsFlux();
  }
}
