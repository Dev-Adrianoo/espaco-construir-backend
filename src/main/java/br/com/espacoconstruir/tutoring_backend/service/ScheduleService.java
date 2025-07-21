package br.com.espacoconstruir.tutoring_backend.service;

import br.com.espacoconstruir.tutoring_backend.dto.BookingRequestDTO;
import br.com.espacoconstruir.tutoring_backend.dto.ScheduleDTO;
import br.com.espacoconstruir.tutoring_backend.exception.ResourceNotFoundException;
import br.com.espacoconstruir.tutoring_backend.model.Schedule;
import br.com.espacoconstruir.tutoring_backend.model.ScheduleModality;
import br.com.espacoconstruir.tutoring_backend.model.ScheduleStatus;
import br.com.espacoconstruir.tutoring_backend.model.Student;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.repository.ScheduleRepository;
import br.com.espacoconstruir.tutoring_backend.repository.StudentRepository;
import br.com.espacoconstruir.tutoring_backend.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ScheduleService {

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private UserRepository userRepository;


  @Value("${N8N_WEBHOOK_CANCELATION_LESSON}")
  private String n8nCancellationWeebhook;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private StudentService studentService;


  @Transactional
  public List<ScheduleDTO> createBooking(BookingRequestDTO bookingRequest) {
    LocalDate date = LocalDate.parse(bookingRequest.getDate());
    LocalTime time = LocalTime.parse(bookingRequest.getTime());
    LocalDateTime startTime = LocalDateTime.of(date, time);
    LocalDateTime endTime = startTime.plusHours(1); // Assumindo que seja 1 hora de duração.

    User guardian = userRepository.findById(Long.parseLong(bookingRequest.getGuardianId()))
        .orElseThrow(() -> new ResourceNotFoundException("Guardian not found"));

    User teacher = null;
    if (bookingRequest.getTeacherId() != null && !bookingRequest.getTeacherId().toString().isEmpty()) {
      teacher = userRepository.findById(Long.parseLong(bookingRequest.getTeacherId().toString()))
          .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
    }

    List<ScheduleDTO> result = new java.util.ArrayList<>();
    for (Long studentId : bookingRequest.getStudentIds()) {
      Student student = studentRepository.findById(studentId)
          .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
      Schedule schedule = new Schedule();
      schedule.setStudent(student);
      schedule.setTeacher(teacher);
      schedule.setStartTime(startTime);
      schedule.setEndTime(endTime);
      schedule.setDifficulties(bookingRequest.getDifficulties());
      schedule.setCondition(bookingRequest.getCondition());
      schedule.setStatus(ScheduleStatus.SCHEDULED);
      schedule.setModality(ScheduleModality.valueOf(bookingRequest.getModality().toUpperCase()));
      schedule.setMeetingLink(bookingRequest.getMeetingLink());
      Schedule savedSchedule = scheduleRepository.save(schedule);
      result.add(convertToDTO(savedSchedule));
    }
    return result;
  }

  @Transactional
  public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
    Student student = studentRepository.findById(scheduleDTO.getStudentId())
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

  public List<Student> getStudentsByTeacherId(Long teacherId) {
    return scheduleRepository.findByTeacherId(teacherId).stream()
        .map(Schedule::getStudent)
        .distinct()
        .collect(Collectors.toList());
  }

  @Transactional
  public ScheduleDTO updateScheduleStatus(Long scheduleId, ScheduleStatus newStatus) {
      
      
      Schedule schedule = scheduleRepository.findById(scheduleId)
              .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado com id: " + scheduleId));

      
      schedule.setStatus(newStatus);
      Schedule updatedSchedule = scheduleRepository.save(schedule);

      
      if (newStatus == ScheduleStatus.CANCELLED) {
          System.out.println("[DEBUG] Status CANCELLED detectado. Preparando para notificar o n8n.");
          
          try {
              User teacher = updatedSchedule.getTeacher();
              Student student = updatedSchedule.getStudent();
              User guardian = student.getGuardian(); 

              if (guardian == null) {
                  throw new RuntimeException("Responsável (Guardian) não encontrado para o aluno: " + student.getName());
              }

              Map<String, Object> payload = buildN8nCancellationPayload(teacher, student, guardian, updatedSchedule);

              HttpHeaders headers = new HttpHeaders();
              headers.setContentType(MediaType.APPLICATION_JSON);
              HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
              
              restTemplate.postForEntity(n8nCancellationWeebhook, request, String.class);
              System.out.println("[DEBUG] Notificação de cancelamento enviada para o n8n com sucesso.");

          } catch (Exception e) {
              System.err.println("[ERRO] Falha ao enviar notificação de cancelamento para o n8n.");
              e.printStackTrace();

          }
      }

      // 4. Retorna a aula atualizada
      return convertToDTO(updatedSchedule);
  }

  private Map<String, Object> buildN8nCancellationPayload(User teacher, Student student, User guardian, Schedule schedule) {
      Map<String, Object> payload = new HashMap<>();
      
      Map<String, Object> teacherMap = new HashMap<>();
      teacherMap.put("id", teacher.getId());
      teacherMap.put("name", teacher.getName());
      payload.put("teacher", teacherMap);

      Map<String, Object> studentMap = new HashMap<>();
      studentMap.put("name", student.getName());
      payload.put("student", studentMap);

      Map<String, Object> guardianMap = new HashMap<>();
      guardianMap.put("id", guardian.getId());
      guardianMap.put("phone", guardian.getPhone());
      payload.put("guardian", guardianMap);

      Map<String, Object> eventDetailsMap = new HashMap<>();
      eventDetailsMap.put("start_time_iso", schedule.getStartTime().toString());
      eventDetailsMap.put("title", "Aula " + student.getName());
      payload.put("event_details", eventDetailsMap);

      return payload;
  }

  @Transactional
  public void deleteSchedule(Long scheduleId) {
    scheduleRepository.deleteById(scheduleId);
  }

  public List<ScheduleDTO> getAllSchedules() {
    return scheduleRepository.findAll()
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  private ScheduleDTO convertToDTO(Schedule schedule) {
    ScheduleDTO dto = new ScheduleDTO();
    dto.setId(schedule.getId());
    dto.setStudentId(schedule.getStudent().getId());
    dto.setStudentName(schedule.getStudent().getName());
    dto.setTeacherId(schedule.getTeacher() != null ? schedule.getTeacher().getId() : null);
    dto.setStartTime(schedule.getStartTime());
    dto.setEndTime(schedule.getEndTime());
    dto.setDifficulties(schedule.getDifficulties());
    dto.setCondition(schedule.getCondition());
    dto.setMeetingLink(schedule.getMeetingLink());
    dto.setStatus(schedule.getStatus());
    dto.setModality(schedule.getModality());
    dto.setSubject(schedule.getSubject());
    dto.setDescription(schedule.getDescription());
    return dto;
  }
}