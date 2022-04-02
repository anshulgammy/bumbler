package dev.bumbler.microservice.student.courses;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentCoursesService {

  private StudentCoursesRepository studentCoursesRepository;

  @Autowired
  public StudentCoursesService(
      StudentCoursesRepository studentCoursesRepository) {
    this.studentCoursesRepository = studentCoursesRepository;
  }

  public List<Course> getAllCourses() {
    return studentCoursesRepository.findAll();
  }

  public Course getCourseById(int id) {
    Optional<Course> course = studentCoursesRepository.findById(id);
    if (course.isPresent()) {
      return course.get();
    } else {
      throw new RuntimeException("No Course Found");
    }
  }

  public void saveCourse(Course newCourse) {
    studentCoursesRepository.save(newCourse);
  }
}
