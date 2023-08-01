/* (C)2023 */
package com.utopiannerd.techcoaching.startup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utopiannerd.techcoaching.dao.model.Batch;
import com.utopiannerd.techcoaching.dao.model.Course;
import com.utopiannerd.techcoaching.dao.model.Student;
import com.utopiannerd.techcoaching.service.BatchService;
import com.utopiannerd.techcoaching.service.CourseService;
import com.utopiannerd.techcoaching.service.StudentService;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class TechCoachingApplicationInitialDataCreator implements CommandLineRunner {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(TechCoachingApplicationInitialDataCreator.class);

  private CourseService courseService;
  private StudentService studentService;
  private BatchService batchService;

  public TechCoachingApplicationInitialDataCreator(
      CourseService courseService, StudentService studentService, BatchService batchService) {
    this.courseService = courseService;
    this.studentService = studentService;
    this.batchService = batchService;
  }

  @Override
  public void run(String... args) throws Exception {
    LOGGER.info("Initial Data Load Started");
    ObjectMapper objectMapper = new ObjectMapper();
    loadCourses(objectMapper);
    loadBatches(objectMapper);
    loadStudents(objectMapper);
    LOGGER.info("Initial Data Load Completed");
  }

  private void loadCourses(ObjectMapper objectMapper) throws IOException {
    List<Course> courseList =
        objectMapper.readValue(
            (new ClassPathResource("data/courses.json")).getInputStream(),
            new TypeReference<List<Course>>() {});
    courseService.addCourses(courseList);
    // .subscribe(course -> LOGGER.info("Course added: {}", course));
    ;
  }

  private void loadBatches(ObjectMapper objectMapper) throws IOException {
    List<Batch> batchList =
        objectMapper.readValue(
            (new ClassPathResource("data/batches.json")).getInputStream(),
            new TypeReference<List<Batch>>() {});
    batchService.addBatches(batchList);
    // .subscribe(batch -> LOGGER.info("Batch added: {}", batch));
  }

  private void loadStudents(ObjectMapper objectMapper) throws IOException {
    List<Student> studentList =
        objectMapper.readValue(
            (new ClassPathResource("data/students.json")).getInputStream(),
            new TypeReference<List<Student>>() {});
    studentService.addStudents(studentList);
    // .subscribe(student -> LOGGER.info("Student added: {}", student));
  }
}
