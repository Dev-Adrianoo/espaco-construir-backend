package br.com.espacoconstruir.tutoring_backend.repository;

import br.com.espacoconstruir.tutoring_backend.model.Student;
import br.com.espacoconstruir.tutoring_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findById(Long id);

    List<Student> findByGuardian(User guardian);

    Optional<Student> findByUser(User user);
}
