package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.ClassHistoryDTO;
import br.com.espacoconstruir.tutoring_backend.service.ClassHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "http://localhost:5173")
public class ClassHistoryController {
  @Autowired
  private ClassHistoryService classHistoryService;

  @PostMapping
  public ResponseEntity<ClassHistoryDTO> save(@RequestBody ClassHistoryDTO dto) {
    return ResponseEntity.ok(classHistoryService.save(dto));
  }

  @GetMapping
  public ResponseEntity<List<ClassHistoryDTO>> getByStudent(@RequestParam Long studentId) {
    return ResponseEntity.ok(classHistoryService.findByStudentId(studentId));
  }
}