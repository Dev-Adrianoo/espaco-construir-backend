package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.BookingRequestDTO;
import br.com.espacoconstruir.tutoring_backend.dto.ScheduleDTO;
import br.com.espacoconstruir.tutoring_backend.model.Schedule;
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
            @RequestParam Schedule.ScheduleStatus status) {
        return ResponseEntity.ok(scheduleService.updateScheduleStatus(scheduleId, status));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok().build();
    }
} 