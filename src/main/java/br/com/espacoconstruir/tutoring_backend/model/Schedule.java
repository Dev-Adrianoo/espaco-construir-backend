package br.com.espacoconstruir.tutoring_backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

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

  @Column(name = "recurrence_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private RecurrenceType recurrenceType;

  @Column(name = "recurrence_id", nullable= true)
  private String recurrenceId;
}