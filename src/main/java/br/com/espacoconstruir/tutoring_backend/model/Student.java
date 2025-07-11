package br.com.espacoconstruir.tutoring_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private String grade;
    private String difficulties;
    private String condition;
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "guardian_id", nullable = true)
    private User guardian;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "registered_by_user_id") 
    private User registeredBy; 

    // Futuro suporte para aula online
    // @Enumerated(EnumType.STRING)
    // private Modality modality;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", grade='" + grade + '\'' +
                ", condition='" + condition + '\'' +
                ", difficulties='" + difficulties + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", guardianId=" + (guardian != null ? guardian.getId() : null) +
                ", userId=" + (user != null ? user.getId() : null) +
                '}';
    }
}
