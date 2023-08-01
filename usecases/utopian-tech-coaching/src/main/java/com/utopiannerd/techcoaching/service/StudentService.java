package com.utopiannerd.techcoaching.service;

import com.utopiannerd.techcoaching.dao.model.Student;
import com.utopiannerd.techcoaching.dao.repository.StudentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class StudentService {

  private StudentRepository studentRepository;

  @Autowired
  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public Mono<ServerResponse> addStudent(ServerRequest serverRequest) {
    return serverRequest
        .bodyToMono(Student.class)
        .map(student -> studentRepository.save(student))
        .flatMap(student -> ServerResponse.ok().body(student, Student.class));
  }

  public List<Student> addStudents(List<Student> studentList) {
    return studentRepository.saveAll(studentList);
  }

  public Student updateStudent(Student student) {
    return studentRepository.save(student);
  }

  public void deleteStudent(Long studentId) {
    studentRepository.deleteById(studentId);
  }

  public Student getStudent(Long studentId) {
    return studentRepository.findById(studentId).orElseThrow();
  }

  public List<Student> getAllStudents() {
    return studentRepository.findAll();
  }
}
