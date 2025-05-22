package br.com.espacoconstruir.tutoring_backend.repository;

import br.com.espacoconstruir.tutoring_backend.model.Student;
import br.com.espacoconstruir.tutoring_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StudentRepository extends JpaRepository<User, Long> {
    List<Student> findByGuardian(User guardian);
}
