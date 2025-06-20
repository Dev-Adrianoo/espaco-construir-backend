package br.com.espacoconstruir.tutoring_backend.service;

import br.com.espacoconstruir.tutoring_backend.model.Class;
import br.com.espacoconstruir.tutoring_backend.model.Student;
import br.com.espacoconstruir.tutoring_backend.dto.ScheduleWithStudentsDTO;
import br.com.espacoconstruir.tutoring_backend.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassService {
  @Autowired
  private ClassRepository classRepository;

  // Implement methods as needed
  public List<Class> getHistoryByStudent(Long alunoId) {
    return Collections.emptyList();
  }

  // Novo método para retornar todos os horários com alunos
  public List<ScheduleWithStudentsDTO> getSchedulesWithStudents() {
    List<Class> allClasses = classRepository.findAll();
    // Agrupa por data+hora
    Map<String, List<Class>> grouped = allClasses.stream()
        .collect(Collectors.groupingBy(c -> c.getDate() + "_" + c.getTime()));
    List<ScheduleWithStudentsDTO> result = new ArrayList<>();
    for (Map.Entry<String, List<Class>> entry : grouped.entrySet()) {
      String[] parts = entry.getKey().split("_");
      String date = parts[0];
      String time = parts[1];
      List<String> alunos = entry.getValue().stream()
          .map(Class::getStudent)
          .filter(Objects::nonNull)
          .map(Student::getName)
          .collect(Collectors.toList());
      ScheduleWithStudentsDTO dto = new ScheduleWithStudentsDTO();
      dto.setDia(date);
      dto.setHora(time);
      dto.setAlunos(alunos);
      result.add(dto);
    }
    return result;
  }
}