package br.com.espacoconstruir.tutoring_backend.service;

import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.model.Role;
import br.com.espacoconstruir.tutoring_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    @Value("${app.frontend-url-prod}")
    private String frontendUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void processForgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail((email));

        if(!userOptional.isPresent()) {
            return;

        }
        User user = userOptional.get();
        String token = UUID.randomUUID().toString();

        user.setPasswordResetToken(token);
        System.out.println("DEBUG: Data atual antes de adicionar minutos: " + LocalDateTime.now());
        user.setPasswordResetExpires(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        String resetLink = frontendUrl + "/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        if (!user.isEmailVerified()) {
            
            throw new DisabledException("E-mail não verificado, Por favor, confirme seu e-mail para fazer login.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    }

    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        user.setEmail(user.getEmail().toLowerCase());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailVerified(false);
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        User savedUser = userRepository.save(user);

        emailService.sendVerificationEmail(savedUser, token);

        return savedUser;

    }

    public User verifyEmail(String token) {

        Optional<User> userOptional = userRepository.findByVerificationToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setEmailVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);
            return user;
        }

        return null;

    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> findAllByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public void resetPassword(String token, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("A nova senha deve ter no mínimo 6 caracteres.");

        }

        Optional<User> userOptional = userRepository.findByPasswordResetToken(token);

        if (!userOptional.isPresent()) {
            throw new RuntimeException("Token inválido ou não encontrado.");
        }

        User user = userOptional.get();

        if (user.getPasswordResetExpires() == null || user.getPasswordResetExpires().isBefore(LocalDateTime.now())) {
        throw new RuntimeException("Token expirado. Por favor, solicite uma nova redefinição.");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));

        user.setPasswordResetToken(null);
        user.setPasswordResetExpires(null);

        userRepository.save(user);
        
    }

}
