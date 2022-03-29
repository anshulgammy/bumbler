package dev.bumbler.springreactiverestapi;

import java.util.Objects;
import javax.annotation.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "studentsCollection")
public class Student {

  @Id
  private long id;
  private String name;
  private long rollNumber;

  public Student(long id, String name, long rollNumber) {
    this.id = id;
    this.name = name;
    this.rollNumber = rollNumber;
  }

  public long getRollNumber() {
    return rollNumber;
  }

  public void setRollNumber(long rollNumber) {
    this.rollNumber = rollNumber;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Student{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", rollNumber=" + rollNumber +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Student student = (Student) o;
    return id == student.id && rollNumber == student.rollNumber && name.equals(student.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, rollNumber);
  }
}
