package br.com.espacoconstruir.tutoring_backend.repository;

import br.com.espacoconstruir.tutoring_backend.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
    List<Class> findByDateAndTime(String date, String time);
}