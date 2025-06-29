package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.BookingRequestDTO;
import br.com.espacoconstruir.tutoring_backend.dto.ScheduleDTO;
import br.com.espacoconstruir.tutoring_backend.dto.ScheduleWithStudentsDTO;
import br.com.espacoconstruir.tutoring_backend.model.ScheduleStatus;
import br.com.espacoconstruir.tutoring_backend.model.Student;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.service.ClassService;
import br.com.espacoconstruir.tutoring_backend.service.ScheduleService;
import br.com.espacoconstruir.tutoring_backend.service.StudentService;
import br.com.espacoconstruir.tutoring_backend.service.UserService;

import org.springframework.http.MediaType; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders; 
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import java.util.HashMap;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

     @Autowired
    private StudentService studentService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${n8n.webhook.url}")
    private String n8nWebhookUrl;

    @PostMapping("/book")
    public ResponseEntity<List<ScheduleDTO>> bookClass(@RequestBody BookingRequestDTO bookingRequest) {
        System.out.println("[DEBUG] Entrou no endpoint /api/schedules/book");

        List<ScheduleDTO> createdSchedules = scheduleService.createBooking(bookingRequest);

        try {
            System.out.println("[DEBUG]- KRATOS PAASSOU POR AQUI\nPreparando para disparar o gatilho do n8n...");

            System.out.println("[DEBUG] - Buscando teacher com ID: " + bookingRequest.getTeacherId());
            User teacher = userService.findById(Long.parseLong(bookingRequest.getTeacherId()));

            System.out.println("[DEBUG]- Buscando guardian com o ID: " + bookingRequest.getGuardianId());
            User guardian = userService.findById(Long.parseLong(bookingRequest.getGuardianId()));

            System.out.println("[DEBUG] - buscando student com o ID: " + bookingRequest.getStudentIds());
            long studentId = bookingRequest.getStudentIds().get(0);
            Student studentEntity = studentService.findById(studentId);
            
            User studentUser  = studentEntity.getUser();

            if (studentUser == null) {          
                throw new RuntimeException("O Aluno com ID " + studentId + " não está associado a um usuário.");

            }

            String startDateTimeISO = bookingRequest.getDate() + "T" + bookingRequest.getTime() + ":00-03:00";
            String durationMinutes = "60";

            Map<String, String> n8nPayload = new HashMap<>();
            n8nPayload.put("studentName", studentUser.getName());
            n8nPayload.put("contactParent", guardian.getPhone());
            n8nPayload.put("teacherName", teacher.getName());
            n8nPayload.put("startDateTime", startDateTimeISO);
            n8nPayload.put("durationMinutes", durationMinutes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<> (n8nPayload, headers);

            restTemplate.postForEntity(n8nWebhookUrl, request, String.class);

            System.out.println("[DEBUG] KRATOS CHEGOU EM SEGURANÇA");
            }catch (Exception e){
                System.err.println("[AVISO] agendamento salvo, mas a chamada para o weebhook do n8n falhou:" + e.getMessage());
            }

        return ResponseEntity.ok((createdSchedules));
    }

    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.createSchedule(scheduleDTO));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ScheduleDTO>> getStudentSchedules(@PathVariable Long studentId) {
        return ResponseEntity.ok(scheduleService.getStudentSchedules(studentId));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<ScheduleDTO>> getTeacherSchedules(@PathVariable Long teacherId) {
        return ResponseEntity.ok(scheduleService.getTeacherSchedules(teacherId));
    }

    @PutMapping("/{scheduleId}/status")
    public ResponseEntity<ScheduleDTO> updateScheduleStatus(
            @PathVariable Long scheduleId,
            @RequestParam ScheduleStatus status) {
        return ResponseEntity.ok(scheduleService.updateScheduleStatus(scheduleId, status));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/with-students")
    public ResponseEntity<List<ScheduleWithStudentsDTO>> getSchedulesWithStudents() {
        return ResponseEntity.ok(classService.getSchedulesWithStudents());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body("Erro interno: " + ex.getMessage());
    }
}