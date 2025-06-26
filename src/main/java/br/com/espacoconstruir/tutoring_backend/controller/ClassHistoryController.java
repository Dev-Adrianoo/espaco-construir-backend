package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.ClassHistoryDTO;
import br.com.espacoconstruir.tutoring_backend.service.ClassHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ClassHistoryController {
  @Autowired
  private ClassHistoryService classHistoryService;

  @PostMapping("/history")
  public ResponseEntity<ClassHistoryDTO> save(@RequestBody ClassHistoryDTO dto) {
    return ResponseEntity.ok(classHistoryService.save(dto));
  }

  @GetMapping("/history")
  public ResponseEntity<List<ClassHistoryDTO>> getByStudentParam(@RequestParam Long studentId) {
    return ResponseEntity.ok(classHistoryService.findByStudentId(studentId));
  }

  @GetMapping("/students/{studentId}/history")
  public ResponseEntity<List<ClassHistoryDTO>> getByStudentPath(@PathVariable Long studentId) {
    return ResponseEntity.ok(classHistoryService.findByStudentId(studentId));
  }
}