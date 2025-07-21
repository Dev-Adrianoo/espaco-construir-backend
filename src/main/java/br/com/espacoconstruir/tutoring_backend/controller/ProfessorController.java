package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.TeacherDTO;
import br.com.espacoconstruir.tutoring_backend.dto.TeacherResponseDTO;
import br.com.espacoconstruir.tutoring_backend.model.Student;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.service.ScheduleService;
import br.com.espacoconstruir.tutoring_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "http://localhost:5173")
public class ProfessorController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/{teacherId}/students")
    public ResponseEntity<List<Student>> getStudentsForTeacher(@PathVariable Long teacherId) {
        List<Student> students = scheduleService.getStudentsByTeacherId(teacherId);
        return ResponseEntity.ok(students);

    }

    // public List<Student> findByTeacherId(Long teacherId) {
    //     return scheduleService.getStudentsByTeacherId(teacherId);
    // }


    @PostMapping("/register")
    public ResponseEntity<?> registerProfessor(@Valid @RequestBody TeacherDTO dto) {
        System.out.println("ProfessorController: Recebida requisição para registrar professor.");
        System.out.println("Dados recebidos: " + dto.getName() + ", " + dto.getEmail() + ", " + dto.getPhone() + ", "
                + dto.getCnpj());
        try {
            User user = new User();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            user.setPhone(dto.getPhone());
            user.setCnpj(dto.getCnpj());
            user.setRole(dto.getRole());
            System.out.println("ProfessorController: Objeto User criado para registro.");
            return ResponseEntity.ok(userService.register(user));
        } catch (RuntimeException e) {
            System.err.println("ProfessorController: Erro capturado em registerProfessor: " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace completo
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) { // Captura qualquer outra exceção genérica
            System.err.println(
                    "ProfessorController: Exceção inesperada capturada em registerProfessor: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno no servidor: " + e.getMessage());
        }
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        System.err.println("ProfessorController: Erro de validação capturado.");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            System.err.println("Erro de validação - Campo: " + fieldName + ", Mensagem: " + errorMessage);
        });
        return errors;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfessor(@PathVariable Long id) {
        try {
            User professor = userService.findById(id);
            return ResponseEntity.ok(professor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TeacherResponseDTO>> listAllProfessors() {
        List<User> professors = userService
                .findAllByRole(br.com.espacoconstruir.tutoring_backend.model.Role.PROFESSORA);
        List<TeacherResponseDTO> response = professors.stream()
                .map(p -> new TeacherResponseDTO(p.getId(), p.getName(), p.getEmail(), p.getPhone(), p.getCnpj(),
                        p.getRole()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email;
            if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
                email = userDetails.getUsername();
            } else if (principal instanceof String str) {
                email = str;
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
            }
            User professor = userService.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Professor not found."));
            TeacherResponseDTO response = new TeacherResponseDTO(
                    professor.getId(),
                    professor.getName(),
                    professor.getEmail(),
                    professor.getPhone(),
                    professor.getCnpj(),
                    professor.getRole());
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Professor not found.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching authenticated professor: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfessor(@PathVariable Long id, @Valid @RequestBody TeacherDTO dto) {
        try {
            User user = userService.findById(id);
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setCnpj(dto.getCnpj());
            return ResponseEntity.ok(userService.update(user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfessor(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
