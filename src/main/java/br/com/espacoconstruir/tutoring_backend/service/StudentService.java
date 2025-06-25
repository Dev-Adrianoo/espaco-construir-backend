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
        // Buscar o responsável primeiro
        User guardian = userService.findById(dto.getGuardianId());
        if (guardian.getRole() != br.com.espacoconstruir.tutoring_backend.model.Role.RESPONSAVEL) {
            throw new RuntimeException("Usuário não é um responsável");
        }

        // Criar estudante
        Student student = new Student();
        student.setName(dto.getName());
        student.setAge(dto.getAge());
        student.setGrade(dto.getGrade());
        student.setCondition(dto.getCondition());
        student.setDifficulties(dto.getDifficulties());
        student.setGuardian(guardian);

        
        User user = new User();
        user.setName(dto.getName());
        user.setRole(br.com.espacoconstruir.tutoring_backend.model.Role.ALUNO);
        
        // Usar email do responsável com sufixo do aluno
        String studentEmail = "aluno." + guardian.getEmail();
        user.setEmail(studentEmail);
        
        // Usar telefone do responsável
        user.setPhone(guardian.getPhone());
        
        // Senha vazia pois aluno não faz login
        user.setPassword("");
        
        user = userService.register(user);
        student.setUser(user);

        studentRepository.save(student);
        return user;
    }

    public List<Student> getByGuardian(User guardian) {
        return studentRepository.findByGuardian(guardian);
    }

    public User findById(Long id) {
        return userService.findById(id);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public List<Student> findByTeacherId(Long teacherId) {
        return scheduleService.getStudentsByTeacherId(teacherId);
    }

    public User update(Long id, StudentDTO dto) {
        // Get the user and update basic info
        User user = userService.findById(id);
        user.setName(dto.getName());

        // Get and update student info
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado"));

        student.setName(dto.getName());
        student.setAge(dto.getAge());
        student.setGrade(dto.getGrade());
        student.setCondition(dto.getCondition());
        student.setDifficulties(dto.getDifficulties());

        // Save both entities
        studentRepository.save(student);
        return userService.update(user);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
        userService.delete(id);
    }
}
