package dev.bumbler.springreactiverestapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class StudentRouter {

  private StudentService studentService;

  @Autowired
  public StudentRouter(StudentService studentService) {
    this.studentService = studentService;
  }

  @Bean
  public RouterFunction<ServerResponse> routerFunction() {
    return RouterFunctions.route()
        .GET("/v1/api/router/students", serverRequest -> {
          return ServerResponse
              .ok()
              .contentType(MediaType.TEXT_EVENT_STREAM)
              .body(studentService.getStudents(), Student.class);
        })
        .GET("/v1/api/router/student/{studentId}", serverRequest -> {
          return ServerResponse
              .ok()
              .contentType(MediaType.TEXT_EVENT_STREAM)
              .body(studentService.getStudent(serverRequest), Student.class);
        })
        .POST("/v1/api/router/student/add", serverRequest -> {
          return ServerResponse
              .ok()
              .contentType(MediaType.TEXT_EVENT_STREAM)
              .body(studentService.addStudent(serverRequest), Student.class);
        })
        .PUT("/v1/api/router/student/update", serverRequest -> {
          return ServerResponse
              .ok()
              .contentType(MediaType.TEXT_EVENT_STREAM)
              .body(studentService.updateStudent(serverRequest), Student.class);
        })
        .DELETE("/v1/api/router/student/delete/{studentId}", serverRequest -> {
          return ServerResponse
              .ok()
              .contentType(MediaType.TEXT_EVENT_STREAM)
              .body(studentService.deleteStudent(serverRequest), Void.class);
        })
        .build();
  }
}
