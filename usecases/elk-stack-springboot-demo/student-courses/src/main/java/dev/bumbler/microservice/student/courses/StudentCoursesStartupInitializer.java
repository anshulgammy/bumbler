package dev.bumbler.microservice.student.courses;

import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class StudentCoursesStartupInitializer {

  private StudentCoursesRepository studentCoursesRepository;

  public StudentCoursesStartupInitializer(
      StudentCoursesRepository studentCoursesRepository) {
    this.studentCoursesRepository = studentCoursesRepository;
  }

  @PostConstruct
  private void loadCoursesIntoDb() {

    List<Course> courseList = Arrays.asList(
        new Course("Java", 23),
        new Course("Spring", 43),
        new Course("Hibernate", 33),
        new Course("React", 65),
        new Course("Design Patterns", 55),
        new Course("Docker", 33));

    studentCoursesRepository.saveAll(courseList);
  }
}
