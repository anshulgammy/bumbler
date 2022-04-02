package dev.bumbler.microservice.student.courses;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCoursesRepository extends CrudRepository<Course, Integer> {

  public List<Course> findAll();

}
