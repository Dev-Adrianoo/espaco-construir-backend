package br.com.espacoconstruir.tutoring_backend.dto;

import java.time.LocalDateTime;

public class StudentResponseDTO {
  private Long id;
  private String name;
  private Integer age;
  private String grade;
  private String difficulties;
  private String condition;
  private Long guardianId;
  private Long registeredBy;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // Constructor vazio
  public StudentResponseDTO() {
  }

  // Constructor com todos os campos
  public StudentResponseDTO(Long id, String name, Integer age, String grade,
      String difficulties, String condition, Long guardianId,
      Long registeredBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.grade = grade;
    this.difficulties = difficulties;
    this.condition = condition;
    this.guardianId = guardianId;
    this.registeredBy = registeredBy;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  // Getters e setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public String getDifficulties() {
    return difficulties;
  }

  public void setDifficulties(String difficulties) {
    this.difficulties = difficulties;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public Long getGuardianId() {
    return guardianId;
  }

  public void setGuardianId(Long guardianId) {
    this.guardianId = guardianId;
  }

  public Long getRegisteredBy() {
    return registeredBy;
  }

  public void setRegisteredBy(Long registeredBy) {
    this.registeredBy = registeredBy;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
