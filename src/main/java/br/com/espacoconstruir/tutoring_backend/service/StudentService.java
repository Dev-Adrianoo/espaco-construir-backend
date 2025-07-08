package br.com.espacoconstruir.tutoring_backend.service;

import br.com.espacoconstruir.tutoring_backend.dto.StudentDTO;
import br.com.espacoconstruir.tutoring_backend.model.Schedule;
import br.com.espacoconstruir.tutoring_backend.model.Student;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.repository.StudentRepository;
import br.com.espacoconstruir.tutoring_backend.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

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
        
        // Gerar um email único para o aluno usando um timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        String studentEmail = "aluno." + timestamp + "." + guardian.getEmail();
        user.setEmail(studentEmail);
        
        // Usar telefone do responsável
        user.setPhone(guardian.getPhone());
        
        // Senha vazia pois aluno não faz login
        user.setPassword("");
        
        // Primeiro registra o usuário
        user = userService.register(user);

        // Depois cria o aluno com o usuário já registrado
        student.setUser(user);
        studentRepository.save(student);
        
        return user;
    }

    public List<Student> getByGuardian(User guardian) {
        return studentRepository.findByGuardian(guardian);
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Estudante com ID" + id + "não encontrado"));
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

    @Transactional
    public void delete(Long studentId) {
  
        Student studentEntity = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("com id " + studentId + " não encontrado"));
        
        List<Schedule> associatedSchedules = scheduleRepository.findByStudent(studentEntity);
        
        for (Schedule schedule : associatedSchedules) {
            
            if (schedule.getStatus() != br.com.espacoconstruir.tutoring_backend.model.ScheduleStatus.CANCELLED) {
            throw new IllegalStateException(
                "Não é possível excluir o aluno, pois ele possui agendamentos ativos ou concluídos. Status encontrado: " + schedule.getStatus()
                );

            }
        }

        long studentUserId = studentEntity.getUser().getId();
        
            if (!associatedSchedules.isEmpty()) {
                scheduleRepository.deleteAll(associatedSchedules);
                System.out.println("✅ Histórico de agendamentos do aluno " + studentId + " limpo.");

            }

            studentRepository.delete(studentEntity);
            System.out.println("✅ Aluno " + studentId + " excluído com sucesso.");

            userService.delete(studentUserId);
            System.out.println("✅ Entidade User do aluno (ID: " + studentUserId + ") removida.");
            System.out.println("✅ O Responsável associado NÃO foi alterado.");
    }
}