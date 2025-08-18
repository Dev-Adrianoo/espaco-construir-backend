package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.GuardianDTO;
import br.com.espacoconstruir.tutoring_backend.dto.GuardianResponseDTO;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.service.UserService;
import br.com.espacoconstruir.tutoring_backend.service.StudentService;
import br.com.espacoconstruir.tutoring_backend.model.Student;
import br.com.espacoconstruir.tutoring_backend.service.ClassService;
import br.com.espacoconstruir.tutoring_backend.model.Class;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import br.com.espacoconstruir.tutoring_backend.dto.StudentResponseDTO;

@RestController
@RequestMapping("/guardians")
public class GuardianController {

  @Autowired
  private UserService userService;

  @Autowired
  private ClassService classService;

  @Autowired
  private StudentService studentService;

  @PostMapping("/register")
  public ResponseEntity<?> registerGuardian(@Valid @RequestBody GuardianDTO dto) {

    System.out.println("[DEBUG] - KRATOS PASSOU POR AQUI!");
    
    try {
      User user = new User();
      user.setName(dto.getName());
      user.setEmail(dto.getEmail());
      user.setPassword(dto.getPassword());
      user.setPhone(dto.getPhone());
      user.setRole(br.com.espacoconstruir.tutoring_backend.model.Role.RESPONSAVEL);

      User savedUser = userService.register(user);
      return ResponseEntity.ok(savedUser);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getGuardian(@PathVariable Long id) {
    try {
      User guardian = userService.findById(id);
      return ResponseEntity.ok(guardian);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateGuardian(@PathVariable Long id, @Valid @RequestBody GuardianDTO dto) {
    try {
      User user = userService.findById(id);
      user.setName(dto.getName());
      user.setEmail(dto.getEmail());
      user.setPhone(dto.getPhone());
      if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
        user.setPassword(dto.getPassword());
      }
      return ResponseEntity.ok(userService.update(user));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteGuardian(@PathVariable Long id) {
    try {
      userService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<List<GuardianResponseDTO>> listAllGuardians() {
    try {
      List<User> guardians = userService.findAllByRole(br.com.espacoconstruir.tutoring_backend.model.Role.RESPONSAVEL);
      List<GuardianResponseDTO> response = guardians.stream()
          .map(g -> new GuardianResponseDTO(g.getId(), g.getName(), g.getEmail(), g.getPhone()))
          .collect(Collectors.toList());
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      System.out.println("Error listing guardians: " + e.getMessage());
      return ResponseEntity.status(500).body(List.of());
    }
  }

  @GetMapping("/history")
  public ResponseEntity<List<Class>> getClassHistory(@RequestParam Long alunoId) {
    List<Class> history = classService.getHistoryByStudent(alunoId);
    return ResponseEntity.ok(history);
  }

  @GetMapping("/children")
  public ResponseEntity<List<StudentResponseDTO>> getChildrenByResponsible(
      @RequestParam(required = false) Long responsavelId) {
    try {
      if (responsavelId == null) {
        return ResponseEntity.badRequest().body(List.of());
      }

      User guardian = userService.findById(responsavelId);
      List<Student> students = studentService.getByGuardian(guardian);
      List<StudentResponseDTO> response = students.stream()
          .map(student -> new StudentResponseDTO(
              student.getId(),
              student.getName(),
              null,
              null,
              student.getAge(),
              student.getGrade(),
              student.getCondition(),
              student.getDifficulties(),
              br.com.espacoconstruir.tutoring_backend.model.Role.ALUNO,
              guardian.getId(),
              null,
              null,
              null,
              new GuardianDTO(guardian.getId(), guardian.getName(), guardian.getEmail(), guardian.getPhone()),
              student.getBirthDate()           
              ))
          .collect(Collectors.toList());
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      System.out.println("Error getting children: " + e.getMessage());
      return ResponseEntity.status(500).body(List.of());
    }
  }

  @GetMapping("/me")
  public ResponseEntity<?> getCurrentGuardian() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) {
      return ResponseEntity.status(401).body("User not authenticated");
    }

    String email = auth.getName();
    return userService.findByEmail(email)
        .map(guardian -> ResponseEntity.ok(new GuardianResponseDTO(
            guardian.getId(),
            guardian.getName(),
            guardian.getEmail(),
            guardian.getPhone())))
        .orElse(ResponseEntity.notFound().build());
  }
}