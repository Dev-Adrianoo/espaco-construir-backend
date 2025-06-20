package br.com.espacoconstruir.tutoring_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class ScheduleWithStudentsDTO {
  private String dia;
  private String hora;
  private List<String> alunos;
}