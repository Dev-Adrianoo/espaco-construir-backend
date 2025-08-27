package br.com.espacoconstruir.tutoring_backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class ScheduleWithStudentsDTO {
  private String dia;
  private String hora;
  private List<String> alunos;
  private List<Long> studentIds;
  private List<Long> scheduleIds;
  private List<String> recurrenceIds;
}