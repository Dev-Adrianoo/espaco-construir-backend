package br.com.espacoconstruir.tutoring_backend.model;

import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(unique = true)
    private String cnpj;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "verification_token")
    private String verificationToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Relacionamento 1:N com student
    @OneToMany(mappedBy = "guardian", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> student = new ArrayList<>();

    // Campos específicos do aluno
    @Transient
    private Integer age;
    @Transient
    private String grade;
    @Transient
    private String condition;
    @Transient
    private String difficulties;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_expires")
    private LocalDateTime passwordResetExpires;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                '}';
    }
}
