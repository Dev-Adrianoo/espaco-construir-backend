package br.com.espacoconstruir.tutoring_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class StudentDTO {
  @NotBlank(message = "Nome é obrigatório")
  private String name;

  @NotNull(message = "ID do responsável é obrigatório")
  private Long guardianId;

  private String grade;
  private String condition;
  private String difficulties;
  private Long teacherId;

  
  @NotNull(message = "Data de nascimento é obrigatória")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  private LocalDate birthDate;

  public StudentDTO() {
  }

  public StudentDTO(String name, Integer age, String grade,
      String difficulties, String condition, Long guardianId) {
    this.name = name;
    this.grade = grade;
    this.difficulties = difficulties;
    this.condition = condition;
    this.guardianId = guardianId;
    this.birthDate = birthDate;
  }

  public Long getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(Long teacherId) {
    this.teacherId = teacherId;
  }

  public String getName() {
    return name;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getGuardianId() {
    return guardianId;
  }

  public void setGuardianId(Long guardianId) {
    this.guardianId = guardianId;
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