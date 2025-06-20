package br.com.espacoconstruir.tutoring_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class BookingRequestDTO {
    private String date; // YYYY-MM-DD format
    private String time; // HH:mm format
    private List<Long> studentIds; // IDs dos alunos (Student)
    private String modality;
    private String guardianId;
    private String teacherId;
    private String difficulties;
    private String condition;
    private String meetingLink;

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
}