package br.com.espacoconstruir.tutoring_backend.dto;

import java.time.LocalDateTime;

import br.com.espacoconstruir.tutoring_backend.model.RecurrenceType;
import br.com.espacoconstruir.tutoring_backend.model.ScheduleModality;
import br.com.espacoconstruir.tutoring_backend.model.ScheduleStatus;
import lombok.Data;

@Data
public class ScheduleDTO {
    private Long id;
    private Long studentId;
    private Long teacherId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String difficulties;
    private String condition;
    private String meetingLink;
    private ScheduleStatus status;
    private ScheduleModality modality;
    private String studentName;
    private String subject;
    private String description;
    private RecurrenceType recurrenceType;
    private String recurrenceId;

    

    public String getDifficulties() {
        return difficulties;
    }

    public void setDifficulties(String difficulties) {
        this.difficulties = difficulties;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}