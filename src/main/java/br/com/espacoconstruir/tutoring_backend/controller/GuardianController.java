package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.GuardianDTO;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guardians")
public class GuardianController {

  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<User> createGuardian(@RequestBody GuardianDTO dto) {
    User user = new User();
    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    user.setPassword(dto.getPassword());
    user.setPhone(dto.getPhone());
    user.setRole(dto.getRole());
    return ResponseEntity.ok(userService.register(user));
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getGuardian(@PathVariable Long id) {
    return ResponseEntity.ok(userService.findById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateGuardian(@PathVariable Long id, @RequestBody GuardianDTO dto) {
    User user = userService.findById(id);
    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    user.setPhone(dto.getPhone());
    return ResponseEntity.ok(userService.update(user));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteGuardian(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}