package dev.bumbler.springreactiverestapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class StudentController {

    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getSyncStudents() {
        return ResponseEntity.ok(studentService.getSyncStudents());
    }

    @GetMapping(value = "/async/students", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<ServerResponse> getAsyncStudents() {
        return studentService.getAsyncStudents();
    }
}
