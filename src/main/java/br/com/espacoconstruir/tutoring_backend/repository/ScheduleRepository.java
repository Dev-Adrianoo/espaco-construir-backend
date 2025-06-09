package br.com.espacoconstruir.tutoring_backend.repository;

import br.com.espacoconstruir.tutoring_backend.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

  List<Schedule> findByStudentId(Long studentId);

  List<Schedule> findByTeacherId(Long teacherId);

  List<Schedule> findByStudentIdAndStartTimeBetween(Long studentId, LocalDateTime start, LocalDateTime end);

  List<Schedule> findByTeacherIdAndStartTimeBetween(Long teacherId, LocalDateTime start, LocalDateTime end);

  boolean existsByStudentIdAndStartTimeBetween(Long studentId, LocalDateTime start, LocalDateTime end);

  boolean existsByTeacherIdAndStartTimeBetween(Long teacherId, LocalDateTime start, LocalDateTime end);
}