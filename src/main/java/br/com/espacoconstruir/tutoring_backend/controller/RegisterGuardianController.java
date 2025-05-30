package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.GuardianDTO;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class RegisterGuardianController {

  @Autowired
  private UserService userService;

  @PostMapping(path = "/api/register/guardians", consumes = "application/json")
  public ResponseEntity<?> registerGuardian(@Valid @RequestBody GuardianDTO dto) {
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
}