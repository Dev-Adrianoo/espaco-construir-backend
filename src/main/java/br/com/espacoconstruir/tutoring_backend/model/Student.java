package br.com.espacoconstruir.tutoring_backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Data
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

    @ManyToOne
    @JoinColumn(name = "guardian_id", nullable = false)
    private User guardian;

    //Future support for online classes
    // @Enumerated(EnumType.STRING)
    // private Modality modality;
}
