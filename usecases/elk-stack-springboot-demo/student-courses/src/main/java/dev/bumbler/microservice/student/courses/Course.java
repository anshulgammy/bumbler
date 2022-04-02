package dev.bumbler.microservice.student.courses;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_COURSES")
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String courseName;

  private double coursePrice;

  public Course() {
  }

  public Course(String courseName, double coursePrice) {
    this.courseName = courseName;
    this.coursePrice = coursePrice;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public double getCoursePrice() {
    return coursePrice;
  }

  public void setCoursePrice(double coursePrice) {
    this.coursePrice = coursePrice;
  }

  @Override
  public String toString() {
    return "Course{" +
        "id=" + id +
        ", courseName='" + courseName + '\'' +
        ", coursePrice=" + coursePrice +
        '}';
  }
}
