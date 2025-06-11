package br.com.espacoconstruir.tutoring_backend.service;

import br.com.espacoconstruir.tutoring_backend.dto.BookingRequestDTO;
import br.com.espacoconstruir.tutoring_backend.dto.ScheduleDTO;
import br.com.espacoconstruir.tutoring_backend.exception.ResourceNotFoundException;
import br.com.espacoconstruir.tutoring_backend.model.Schedule;
import br.com.espacoconstruir.tutoring_backend.model.ScheduleModality;
import br.com.espacoconstruir.tutoring_backend.model.ScheduleStatus;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.repository.ScheduleRepository;
import br.com.espacoconstruir.tutoring_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private UserRepository userRepository;

  @Transactional
  public ScheduleDTO createBooking(BookingRequestDTO bookingRequest) {
    // Parse date and time
    LocalDate date = LocalDate.parse(bookingRequest.getDate());
    LocalTime time = LocalTime.parse(bookingRequest.getTime());
    LocalDateTime startTime = LocalDateTime.of(date, time);
    LocalDateTime endTime = startTime.plusHours(1); // Assuming 1-hour classes

    // Find student (child)
    User student = userRepository.findById(Long.parseLong(bookingRequest.getChildId()))
        .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

    // Find guardian
    User guardian = userRepository.findById(Long.parseLong(bookingRequest.getGuardianId()))
        .orElseThrow(() -> new ResourceNotFoundException("Guardian not found"));

    // Find teacher (optional)
    User teacher = null;
    if (bookingRequest.getTeacherId() != null && !bookingRequest.getTeacherId().isEmpty()) {
      teacher = userRepository.findById(Long.parseLong(bookingRequest.getTeacherId()))
          .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
    }

    // Check for scheduling conflicts
    if (scheduleRepository.existsByStudentIdAndStartTimeBetween(
        student.getId(), startTime, endTime)) {
      throw new RuntimeException("Student already has a schedule in this time slot");
    }

    // Check for teacher scheduling conflicts only if a teacher is assigned
    if (teacher != null && scheduleRepository.existsByTeacherIdAndStartTimeBetween(
        teacher.getId(), startTime, endTime)) {
      throw new RuntimeException("Teacher already has a schedule in this time slot");
    }

    // Create schedule
    Schedule schedule = new Schedule();
    schedule.setStudent(student);
    schedule.setTeacher(teacher);
    schedule.setStartTime(startTime);
    schedule.setEndTime(endTime);
    schedule.setSubject("Default Subject"); // TODO: Get subject from somewhere
    schedule.setDescription("Booking for " + bookingRequest.getChildName());
    schedule.setStatus(ScheduleStatus.SCHEDULED);
    schedule.setModality(ScheduleModality.valueOf(bookingRequest.getModality().toUpperCase()));
    // TODO: Generate or get meeting link
    schedule.setMeetingLink("https://meet.google.com/xxx-yyyy-zzz");

    Schedule savedSchedule = scheduleRepository.save(schedule);
    return convertToDTO(savedSchedule);
  }

  @Transactional
  public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
    User student = userRepository.findById(scheduleDTO.getStudentId())
        .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    User teacher = userRepository.findById(scheduleDTO.getTeacherId())
        .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

    if (scheduleRepository.existsByStudentIdAndStartTimeBetween(
        student.getId(), scheduleDTO.getStartTime(), scheduleDTO.getEndTime())) {
      throw new RuntimeException("Student already has a schedule in this time slot");
    }

    if (scheduleRepository.existsByTeacherIdAndStartTimeBetween(
        teacher.getId(), scheduleDTO.getStartTime(), scheduleDTO.getEndTime())) {
      throw new RuntimeException("Teacher already has a schedule in this time slot");
    }

    Schedule schedule = new Schedule();
    schedule.setStudent(student);
    schedule.setTeacher(teacher);
    schedule.setStartTime(scheduleDTO.getStartTime());
    schedule.setEndTime(scheduleDTO.getEndTime());
    schedule.setSubject(scheduleDTO.getSubject());
    schedule.setDescription(scheduleDTO.getDescription());
    schedule.setStatus(ScheduleStatus.SCHEDULED);
    schedule.setMeetingLink(scheduleDTO.getMeetingLink());

    Schedule savedSchedule = scheduleRepository.save(schedule);
    return convertToDTO(savedSchedule);
  }

  public List<ScheduleDTO> getStudentSchedules(Long studentId) {
    return scheduleRepository.findByStudentId(studentId)
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public List<ScheduleDTO> getTeacherSchedules(Long teacherId) {
    return scheduleRepository.findByTeacherId(teacherId)
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Transactional
  public ScheduleDTO updateScheduleStatus(Long scheduleId, ScheduleStatus newStatus) {
    Schedule schedule = scheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
    schedule.setStatus(newStatus);
    return convertToDTO(scheduleRepository.save(schedule));
  }

  @Transactional
  public void deleteSchedule(Long scheduleId) {
    scheduleRepository.deleteById(scheduleId);
  }

  private ScheduleDTO convertToDTO(Schedule schedule) {
    ScheduleDTO dto = new ScheduleDTO();
    dto.setId(schedule.getId());
    dto.setStudentId(schedule.getStudent().getId());
    dto.setTeacherId(schedule.getTeacher() != null ? schedule.getTeacher().getId() : null);
    dto.setStartTime(schedule.getStartTime());
    dto.setEndTime(schedule.getEndTime());
    dto.setSubject(schedule.getSubject());
    dto.setDescription(schedule.getDescription());
    dto.setStatus(schedule.getStatus());
    dto.setMeetingLink(schedule.getMeetingLink());
    dto.setModality(schedule.getModality());
    return dto;
  }
}