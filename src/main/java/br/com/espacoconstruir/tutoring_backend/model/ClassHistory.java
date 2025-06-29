package br.com.espacoconstruir.tutoring_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "class_history")
public class ClassHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "student_id", nullable = false)
  private Student student;

  @ManyToOne
  @JoinColumn(name = "teacher_id", nullable = false)
  private User teacher;

  @ManyToOne
  @JoinColumn(name = "class_id", nullable = true)
  private Class aClass;

  @Column(columnDefinition = "TEXT")
  private String comment;

  private LocalDateTime createdAt = LocalDateTime.now();

  // Getters e setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student2) {
    this.student = student2;
  }

  public User getTeacher() {
    return teacher;
  }

  public void setTeacher(User teacher) {
    this.teacher = teacher;
  }

  public Class getaClass() {
    return aClass;
  }

  public void setaClass(Class aClass) {
    this.aClass = aClass;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}