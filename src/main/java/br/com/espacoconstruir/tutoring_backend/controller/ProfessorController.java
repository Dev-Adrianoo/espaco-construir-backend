package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.TeacherDTO;
import br.com.espacoconstruir.tutoring_backend.dto.TeacherResponseDTO;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/professors")
@CrossOrigin(origins = "http://localhost:5173")
public class ProfessorController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerProfessor(@Valid @RequestBody TeacherDTO dto) {
        try {
            User user = new User();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            user.setPhone(dto.getPhone());
            user.setCnpj(dto.getCnpj());
            user.setRole(dto.getRole());
            return ResponseEntity.ok(userService.register(user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
