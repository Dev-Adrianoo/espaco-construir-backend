package br.com.espacoconstruir.tutoring_backend.dto;

import br.com.espacoconstruir.tutoring_backend.model.ScheduleStatus;
import br.com.espacoconstruir.tutoring_backend.model.ScheduleModality;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDTO {
    private Long id;
    private Long studentId;
    private Long teacherId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String subject;
    private String description;
    private ScheduleStatus status;
    private String meetingLink;
    private ScheduleModality modality;
    private String studentName;
}