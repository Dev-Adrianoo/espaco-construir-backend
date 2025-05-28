package br.com.espacoconstruir.tutoring_backend.service;

import br.com.espacoconstruir.tutoring_backend.dto.TeacherDTO;
import br.com.espacoconstruir.tutoring_backend.model.Teacher;
import br.com.espacoconstruir.tutoring_backend.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

  @Autowired
  private TeacherRepository teacherRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public Teacher registerTeacher(TeacherDTO teacherDTO) {

    if (teacherRepository.findByEmail(teacherDTO.getEmail()).isPresent()) {
      throw new RuntimeException("Email já cadastrado");
    }

    if (teacherRepository.findByCnpj(teacherDTO.getCnpj()).isPresent()) {
      throw new RuntimeException("CNPJ já cadastrado");
    }

    Teacher teacher = new Teacher();
    teacher.setName(teacherDTO.getName());
    teacher.setEmail(teacherDTO.getEmail());
    teacher.setPassword(passwordEncoder.encode(teacherDTO.getPassword()));
    teacher.setPhone(teacherDTO.getPhone());
    teacher.setCnpj(teacherDTO.getCnpj());
    teacher.setRole(teacherDTO.getRole());

    return teacherRepository.save(teacher);
  }
}