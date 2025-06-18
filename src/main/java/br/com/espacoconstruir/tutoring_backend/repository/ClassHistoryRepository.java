package br.com.espacoconstruir.tutoring_backend.repository;

import br.com.espacoconstruir.tutoring_backend.model.ClassHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClassHistoryRepository extends JpaRepository<ClassHistory, Long> {
  List<ClassHistory> findByStudentIdOrderByCreatedAtDesc(Long studentId);
}