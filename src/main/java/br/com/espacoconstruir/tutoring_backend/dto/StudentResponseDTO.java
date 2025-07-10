package br.com.espacoconstruir.tutoring_backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import br.com.espacoconstruir.tutoring_backend.model.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

public class StudentResponseDTO {
  private Long id;
  private String name;
  private String email;
  private String phone;
  private Integer age;
  private String grade;
  private String condition;
  private String difficulties;
  private Role role;
  private Long guardianId;
  private Long registeredBy;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private GuardianDTO guardian;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  private LocalDate birthDate;

  // Constructor vazio
  public StudentResponseDTO() {
  }

  // Constructor com todos os campos
  public StudentResponseDTO(Long id, String name, String email, String phone, Integer age,
      String grade, String condition, String difficulties, Role role, Long guardianId,
      Long registeredBy, LocalDateTime createdAt, LocalDateTime updatedAt, GuardianDTO guardian, LocalDate birthDate) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.age = age;
    this.grade = grade;
    this.condition = condition;
    this.difficulties = difficulties;
    this.role = role;
    this.guardianId = guardianId;
    this.registeredBy = registeredBy;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.guardian = guardian;
    this.birthDate = birthDate;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
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

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public String getDifficulties() {
    return difficulties;
  }

  public void setDifficulties(String difficulties) {
    this.difficulties = difficulties;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
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

  public GuardianDTO getGuardian() {
    return guardian;
  }

  public void setGuardian(GuardianDTO guardian) {
    this.guardian = guardian;
  }
}
