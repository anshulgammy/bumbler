package com.utopiannerd.techcoaching.router;

import static com.utopiannerd.techcoaching.router.RouterConfiguration.Constants.FETCH_ALL_STUDENTS_ENDPOINT;
import static com.utopiannerd.techcoaching.router.RouterConfiguration.Constants.SAVE_STUDENT_ENDPOINT;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import com.utopiannerd.techcoaching.service.StudentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

  private StudentService studentService;

  public RouterConfiguration(StudentService studentService) {
    this.studentService = studentService;
  }

  @Bean
  public RouterFunction<ServerResponse> routerFunction() {
    return RouterFunctions.route()
        .GET(
            FETCH_ALL_STUDENTS_ENDPOINT,
            serverRequest -> ServerResponse.ok().body(fromValue(studentService.getAllStudents())))
        .POST(SAVE_STUDENT_ENDPOINT, serverRequest -> studentService.addStudent(serverRequest))
        .build();
  }

  public static final class Constants {
    public static final String FETCH_ALL_STUDENTS_ENDPOINT = "/students/all";
    public static final String SAVE_STUDENT_ENDPOINT = "/students/save";
  }
}
