package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.GuardianDTO;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/guardians")
@CrossOrigin(origins = "http://localhost:5173")
public class GuardianController {

  @Autowired
  private UserService userService;

  @PostMapping("/register")
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
}