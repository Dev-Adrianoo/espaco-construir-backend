package br.com.espacoconstruir.tutoring_backend.service;

import br.com.espacoconstruir.tutoring_backend.dto.StudentDTO;
import br.com.espacoconstruir.tutoring_backend.model.Role;
import br.com.espacoconstruir.tutoring_backend.model.Schedule;
import br.com.espacoconstruir.tutoring_backend.model.Student;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.repository.StudentRepository;
import br.com.espacoconstruir.tutoring_backend.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

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

        User responsibleUser = userService.findById(dto.getGuardianId());

        if (responsibleUser.getRole() != Role.PROFESSORA && responsibleUser.getRole() != Role.RESPONSAVEL) {
            throw new RuntimeException("O usuário associado ao cadastro do aluno precisa ser um Responsável ou uma Professora");
        }

        // criando user para o Aluno
        User studentUser = new User();
        studentUser.setName(dto.getName());
        studentUser.setRole(br.com.espacoconstruir.tutoring_backend.model.Role.ALUNO);
        studentUser.setPassword("");

        // Gerar um email único para o aluno usando um timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        String studentEmail = "aluno." + timestamp + "@default.com";
        studentUser.setEmail(studentEmail);
        studentUser.setPhone(responsibleUser.getPhone());

        User createdStudentUser = userService.register(studentUser);

        // Criar a entidade do
        Student student = new Student();
        student.setName(dto.getName());
        student.setGrade(dto.getGrade());
        student.setCondition(dto.getCondition());
        student.setDifficulties(dto.getDifficulties());
        student.setBirthDate(dto.getBirthDate());
        student.setUser(createdStudentUser);
        student.setGuardian(responsibleUser);

        

        studentRepository.save(student);

        return createdStudentUser;
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

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado com id: " + id));

        User user = student.getUser();

        user.setName(dto.getName());

        student.setName(dto.getName());

        student.setGrade(dto.getGrade());
        student.setCondition(dto.getCondition());
        student.setDifficulties(dto.getDifficulties());
        student.setBirthDate(dto.getBirthDate());

        if (dto.getBirthDate() != null) {
            student.setAge(Period.between(dto.getBirthDate(), LocalDate.now()).getYears());
        }

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
                        "Não é possível excluir o aluno, pois ele possui agendamentos ativos ou concluídos. Status encontrado: "
                                + schedule.getStatus());

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