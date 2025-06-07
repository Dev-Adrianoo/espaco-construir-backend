package br.com.espacoconstruir.tutoring_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class StudentDTO {
  @NotBlank(message = "Nome é obrigatório")
  private String name;

  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email inválido")
  private String email;

  @NotBlank(message = "Senha é obrigatória")
  private String password;

  @Pattern(regexp = "^\\d{10,11}$", message = "Telefone deve ter 10 ou 11 dígitos")
  private String phone;

  @NotNull(message = "ID do responsável é obrigatório")
  private Long guardianId;

  private Integer age;
  private String grade;
  private String condition;
  private String difficulties;

  public StudentDTO() {
  }

  public StudentDTO(String name, Integer age, String grade,
      String difficulties, String condition, Long guardianId) {
    this.name = name;
    this.age = age;
    this.grade = grade;
    this.difficulties = difficulties;
    this.condition = condition;
    this.guardianId = guardianId;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Long getGuardianId() {
    return guardianId;
  }

  public void setGuardianId(Long guardianId) {
    this.guardianId = guardianId;
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
}