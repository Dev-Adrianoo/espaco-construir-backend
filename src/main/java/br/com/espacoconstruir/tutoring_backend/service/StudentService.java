package br.com.espacoconstruir.tutoring_backend.service;

import br.com.espacoconstruir.tutoring_backend.dto.StudentDTO;
import br.com.espacoconstruir.tutoring_backend.model.Student;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScheduleService scheduleService;

    public User register(StudentDTO dto) {
        // 1. Criar usuário
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhone(dto.getPhone());
        user.setRole(br.com.espacoconstruir.tutoring_backend.model.Role.ALUNO);
        user = userService.register(user);

        // 2. Criar estudante
        Student student = new Student();
        student.setName(dto.getName());
        student.setAge(dto.getAge());
        student.setGrade(dto.getGrade());
        student.setCondition(dto.getCondition());
        student.setDifficulties(dto.getDifficulties());

        // Buscar o responsável
        User guardian = userService.findById(dto.getGuardianId());
        if (guardian.getRole() != br.com.espacoconstruir.tutoring_backend.model.Role.RESPONSAVEL) {
            throw new RuntimeException("Usuário não é um responsável");
        }
        student.setGuardian(guardian);

        studentRepository.save(student);

        return user;
    }

    public List<Student> getByGuardian(User guardian) {
        return studentRepository.findByGuardian(guardian);
    }

    public User findById(Long id) {
        return userService.findById(id);
    }

    public List<User> findAll() {
        return userService.findAllByRole(br.com.espacoconstruir.tutoring_backend.model.Role.ALUNO);
    }

    public List<User> findByTeacherId(Long teacherId) {
        return scheduleService.getStudentsByTeacherId(teacherId);
    }

    public User update(Long id, StudentDTO dto) {
        User user = userService.findById(id);
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado"));

        student.setName(dto.getName());
        student.setAge(dto.getAge());
        student.setGrade(dto.getGrade());
        student.setCondition(dto.getCondition());
        student.setDifficulties(dto.getDifficulties());

        studentRepository.save(student);
        return userService.update(user);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
        userService.delete(id);
    }
}
