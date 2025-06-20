package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.BookingRequestDTO;
import br.com.espacoconstruir.tutoring_backend.dto.ScheduleDTO;
import br.com.espacoconstruir.tutoring_backend.dto.ScheduleWithStudentsDTO;
import br.com.espacoconstruir.tutoring_backend.model.ScheduleStatus;
import br.com.espacoconstruir.tutoring_backend.service.ClassService;
import br.com.espacoconstruir.tutoring_backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ClassService classService;

    @PostMapping("/book")
    public ResponseEntity<ScheduleDTO> bookClass(@RequestBody BookingRequestDTO bookingRequest) {
        return ResponseEntity.ok(scheduleService.createBooking(bookingRequest));
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
}