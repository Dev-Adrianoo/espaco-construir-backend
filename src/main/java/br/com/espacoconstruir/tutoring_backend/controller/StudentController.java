package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.StudentDTO;
import br.com.espacoconstruir.tutoring_backend.dto.StudentResponseDTO;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import br.com.espacoconstruir.tutoring_backend.dto.GuardianDTO;
import br.com.espacoconstruir.tutoring_backend.repository.StudentRepository;
import br.com.espacoconstruir.tutoring_backend.model.Student;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody StudentDTO dto) {
        try {
            return ResponseEntity.ok(studentService.register(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        try {
            Student student = studentService.findById(id);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> listAllStudents() {
        List<Student> students = studentRepository.findAll();
        List<StudentResponseDTO> response = students.stream()
                .map(student -> {
                    User user = student.getUser();
                    User guardian = student.getGuardian();
                    GuardianDTO guardianDTO = guardian != null ? new GuardianDTO(
                            guardian.getId(),
                            guardian.getName(),
                            guardian.getEmail(),
                            guardian.getPhone()) : null;
                    return new StudentResponseDTO(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getPhone(),
                            student.getAge(),
                            student.getGrade(),
                            student.getCondition(),
                            student.getDifficulties(),
                            user.getRole(),
                            guardian != null ? guardian.getId() : null,
                            null, // registeredBy
                            null, // createdAt
                            null, // updatedAt
                            guardianDTO);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByTeacherId(@PathVariable Long teacherId) {
        try {
            List<Student> students = studentService.findByTeacherId(teacherId);
            List<StudentResponseDTO> response = students.stream()
                    .map(student -> {
                        User user = student.getUser();
                        User guardian = student.getGuardian();
                        GuardianDTO guardianDTO = guardian != null ? new GuardianDTO(
                                guardian.getId(),
                                guardian.getName(),
                                guardian.getEmail(),
                                guardian.getPhone()) : null;
                        return new StudentResponseDTO(
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getPhone(),
                                student.getAge(),
                                student.getGrade(),
                                student.getCondition(),
                                student.getDifficulties(),
                                user.getRole(),
                                guardian != null ? guardian.getId() : null,
                                null, // registeredBy
                                null, // createdAt
                                null, // updatedAt
                                guardianDTO);
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(java.util.Collections.emptyList());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO dto) {
        try {
            return ResponseEntity.ok(studentService.update(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}