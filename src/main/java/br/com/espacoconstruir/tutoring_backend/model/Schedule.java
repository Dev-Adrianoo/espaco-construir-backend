package br.com.espacoconstruir.tutoring_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "schedules")
public class Schedule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "student_id", nullable = false)
  private Student student;

  @ManyToOne
  @JoinColumn(name = "teacher_id", nullable = true)
  private User teacher;

  @Column(nullable = false)
  private LocalDateTime startTime;

  @Column(nullable = false)
  private LocalDateTime endTime;

  @Column(nullable = false)
  private String subject;

  @Column
  private String description;

  @Column(nullable = true)
  private String meetingLink;

  @Column(nullable = true)
  private String difficulties;

  @Column(nullable = true)
  private String condition;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ScheduleStatus status;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ScheduleModality modality;
}