package dev.bumbler.microservice.student.courses;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
public class StudentCoursesController {

  private final Logger LOGGER = LogManager.getLogger(this.getClass());
  
  private StudentCoursesService studentCoursesService;

  @Autowired
  public StudentCoursesController(
      StudentCoursesService studentCoursesService) {
    this.studentCoursesService = studentCoursesService;
  }

  @GetMapping("/courses")
  public ResponseEntity<List<Course>> getAllCourses() {
    LOGGER.info("Request to fetch all courses");
    return ResponseEntity.ok(studentCoursesService.getAllCourses());
  }

  @GetMapping("/courses/{id}")
  public ResponseEntity<Course> getCourseById(@PathVariable int id) {
    LOGGER.info("Request to fetch course for the id: {}", id);
    return ResponseEntity.ok(studentCoursesService.getCourseById(id));
  }

  @PostMapping("/courses/add")
  public void saveCourse(@RequestBody Course newCourse) {
    LOGGER.info("Request to save new course: {}", newCourse);
    studentCoursesService.saveCourse(newCourse);
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<String> exceptionHandler(Exception ex) {
    String errorMessage =
        "Unable to process right now. Please try again after some time. \nTraceMessage: "
            + ex.getMessage();
    LOGGER.error(errorMessage);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
  }
}
