package br.com.espacoconstruir.tutoring_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import br.com.espacoconstruir.tutoring_backend.model.Role;

public class AuthRequestDTO {
  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email inválido")
  private String email;

  @NotBlank(message = "Senha é obrigatória")
  private String password;

  @NotBlank(message = "Tipo de usuário é obrigatório")
  private String userType;

  public AuthRequestDTO() {
  }

  public AuthRequestDTO(String email, String password, String userType) {
    this.email = email;
    this.password = password;
    this.userType = userType;
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

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public Role getRole() {
    return switch (userType.toLowerCase()) {
      case "parent", "responsavel" -> Role.RESPONSAVEL;
      case "teacher", "professor", "professora" -> Role.PROFESSORA;
      case "student", "aluno" -> Role.ALUNO;
      default -> throw new IllegalArgumentException("Tipo de usuário inválido: " + userType);
    };
  }
}