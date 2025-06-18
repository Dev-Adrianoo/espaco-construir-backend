package br.com.espacoconstruir.tutoring_backend.dto;

import java.time.LocalDateTime;

public class ClassHistoryDTO {
  private Long id;
  private Long studentId;
  private Long teacherId;
  private Long classId;
  private String comment;
  private LocalDateTime createdAt;

  public ClassHistoryDTO() {
  }

  public ClassHistoryDTO(Long id, Long studentId, Long teacherId, Long classId, String comment,
      LocalDateTime createdAt) {
    this.id = id;
    this.studentId = studentId;
    this.teacherId = teacherId;
    this.classId = classId;
    this.comment = comment;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public Long getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(Long teacherId) {
    this.teacherId = teacherId;
  }

  public Long getClassId() {
    return classId;
  }

  public void setClassId(Long classId) {
    this.classId = classId;
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