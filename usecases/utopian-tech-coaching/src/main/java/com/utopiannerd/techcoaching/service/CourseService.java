/* (C)2023 */
package com.utopiannerd.techcoaching.service;

import com.utopiannerd.techcoaching.dao.model.Course;
import com.utopiannerd.techcoaching.dao.repository.CourseRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

  private CourseRepository courseRepository;

  @Autowired
  public CourseService(CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  public Course addCourse(Course course) {
    return courseRepository.save(course);
  }

  public List<Course> addCourses(List<Course> courseList) {
    return courseRepository.saveAll(courseList);
  }

  public Course updateCourse(Course course) {
    return courseRepository.save(course);
  }

  public void deleteCourse(Long courseId) {
    courseRepository.deleteById(courseId);
  }

  public Course getCourse(Long courseId) {
    return courseRepository.findById(courseId).orElseThrow();
  }
}
