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
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/guardians")
@CrossOrigin(origins = "http://localhost:5173")
public class GuardianController {

  @Autowired
  private UserService userService;

  @Autowired
  private ClassService classService;

  @Autowired
  private StudentService studentService;

  @PostMapping
  public ResponseEntity<?> createGuardian(@Valid @RequestBody GuardianDTO dto) {
    try {
      User user = new User();
      user.setName(dto.getName());
      user.setEmail(dto.getEmail());
      user.setPassword(dto.getPassword());
      user.setPhone(dto.getPhone());
      user.setRole(dto.getRole());

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
    List<User> guardians = userService.findAllByRole(br.com.espacoconstruir.tutoring_backend.model.Role.RESPONSAVEL);
    List<GuardianResponseDTO> response = guardians.stream()
        .map(g -> new GuardianResponseDTO(g.getId(), g.getName(), g.getEmail(), g.getPhone()))
        .collect(Collectors.toList());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/history")
  public ResponseEntity<List<Class>> getClassHistory(@RequestParam Long alunoId) {
    List<Class> history = classService.getHistoryByStudent(alunoId);
    return ResponseEntity.ok(history);
  }

  @GetMapping
  public ResponseEntity<List<Student>> getChildrenByResponsible(@RequestParam Long responsavelId) {
    User guardian = userService.findById(responsavelId);
    List<Student> students = studentService.getByGuardian(guardian);
    return ResponseEntity.ok(students);
  }
}