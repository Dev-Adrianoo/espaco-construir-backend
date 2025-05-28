package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.TeacherDTO;
import br.com.espacoconstruir.tutoring_backend.model.Teacher;
import br.com.espacoconstruir.tutoring_backend.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/register")
    public ResponseEntity<Teacher> registerTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        Teacher teacher = teacherService.registerTeacher(teacherDTO);
        return ResponseEntity.ok(teacher);
    }
} 