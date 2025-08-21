package br.com.espacoconstruir.tutoring_backend.repository;

import br.com.espacoconstruir.tutoring_backend.model.Schedule;
import br.com.espacoconstruir.tutoring_backend.model.ScheduleStatus;
import br.com.espacoconstruir.tutoring_backend.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

  @Query("SELECT DISTINCT s.student FROM Schedule s WHERE s.teacher.id = :teacherId")
  List<Student> findDistinctStudentsByTeacherId(@Param("teacherId") Long teacherId);

  List<Schedule> findByStudentId(Long studentId);

  List<Schedule> findByTeacherId(Long teacherId);

  List<Schedule> findByStudent(Student student);

  List<Schedule> findByStudentIdAndStartTimeBetween(Long studentId, LocalDateTime start, LocalDateTime end);

  List<Schedule> findByTeacherIdAndStartTimeBetween(Long teacherId, LocalDateTime start, LocalDateTime end);

  boolean existsByStudentIdAndStartTimeBetween(Long studentId, LocalDateTime start, LocalDateTime end);

  boolean existsByTeacherIdAndStartTimeBetween(Long teacherId, LocalDateTime start, LocalDateTime end);

  List<Schedule> findAllByStatusNot(ScheduleStatus status);

  List<Schedule> findByTeacherIdAndStatusNot(Long teacherId, ScheduleStatus status);
}