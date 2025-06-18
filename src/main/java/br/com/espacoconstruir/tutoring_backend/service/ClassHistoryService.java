package br.com.espacoconstruir.tutoring_backend.service;

import br.com.espacoconstruir.tutoring_backend.dto.ClassHistoryDTO;
import br.com.espacoconstruir.tutoring_backend.model.ClassHistory;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.model.Class;
import br.com.espacoconstruir.tutoring_backend.repository.ClassHistoryRepository;
import br.com.espacoconstruir.tutoring_backend.repository.UserRepository;
import br.com.espacoconstruir.tutoring_backend.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassHistoryService {
  @Autowired
  private ClassHistoryRepository classHistoryRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ClassRepository classRepository;

  public ClassHistoryDTO save(ClassHistoryDTO dto) {
    ClassHistory history = new ClassHistory();
    User student = userRepository.findById(dto.getStudentId())
        .orElseThrow(() -> new RuntimeException("Student not found"));
    User teacher = userRepository.findById(dto.getTeacherId())
        .orElseThrow(() -> new RuntimeException("Teacher not found"));
    history.setStudent(student);
    history.setTeacher(teacher);
    if (dto.getClassId() != null) {
      Class aClass = classRepository.findById(dto.getClassId())
          .orElse(null);
      history.setaClass(aClass);
    }
    history.setComment(dto.getComment());
    ClassHistory saved = classHistoryRepository.save(history);
    return toDTO(saved);
  }

  public List<ClassHistoryDTO> findByStudentId(Long studentId) {
    return classHistoryRepository.findByStudentIdOrderByCreatedAtDesc(studentId)
        .stream().map(this::toDTO).collect(Collectors.toList());
  }

  private ClassHistoryDTO toDTO(ClassHistory history) {
    return new ClassHistoryDTO(
        history.getId(),
        history.getStudent().getId(),
        history.getTeacher().getId(),
        history.getaClass() != null ? history.getaClass().getId() : null,
        history.getComment(),
        history.getCreatedAt());
  }
}