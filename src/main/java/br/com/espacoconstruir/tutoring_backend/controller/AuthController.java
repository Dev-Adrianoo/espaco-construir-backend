package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.AuthRequestDTO;
import br.com.espacoconstruir.tutoring_backend.dto.AuthResponseDTO;
import br.com.espacoconstruir.tutoring_backend.dto.TokenRequestDTO;
import br.com.espacoconstruir.tutoring_backend.model.User;
import br.com.espacoconstruir.tutoring_backend.service.JwtService;
import br.com.espacoconstruir.tutoring_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

import br.com.espacoconstruir.tutoring_backend.dto.ResetPasswordRequestDTO;


class ForgotPasswordRequest {
    private String email;
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
}



@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/reset-password") 
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO requestDTO) {
        try {
            userService.resetPassword(requestDTO.getToken(), requestDTO.getNewPassword());
            return ResponseEntity.ok(("Senha redefinida com sucesso."));

        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request){

        userService.processForgotPassword(request.getEmail());

        return ResponseEntity.ok("Link de recuperação enviado");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDTO request) {
        try {
            User user = userService.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            if (!user.getRole().equals(request.getRole())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Tipo de usuário incorreto. Você está tentando acessar como " +
                                request.getUserType() + " mas sua conta é do tipo " + user.getRole());
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body("Senha incorreta");
            }

            UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
            String token = jwtService.generateToken(userDetails);
            AuthResponseDTO response = new AuthResponseDTO(
                    token,
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestBody TokenRequestDTO request) {
        try {
            String email = jwtService.extractUsername(request.getToken());
            UserDetails userDetails = userService.loadUserByUsername(email);

            if (email == null || !jwtService.isTokenValid(request.getToken(), userDetails)) {
                return ResponseEntity.badRequest().body("Token inválido ou expirado");
            }

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            AuthResponseDTO response = new AuthResponseDTO(
                    request.getToken(),
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao verificar token: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRequestDTO request) {
        try {
            String email = jwtService.extractUsername(request.getToken());
            UserDetails userDetails = userService.loadUserByUsername(email);
            if (email == null || !jwtService.isRefreshTokenValid(request.getToken(), userDetails)) {
                return ResponseEntity.badRequest().body("Refresh token inválido ou expirado");
            }
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            String newAccessToken = jwtService.generateToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);
            AuthResponseDTO response = new AuthResponseDTO(
                    newAccessToken,
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole());
            Map<String, Object> result = new HashMap<>();
            result.put("accessToken", newAccessToken);
            result.put("refreshToken", newRefreshToken);
            result.put("user", response);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao renovar token: " + e.getMessage());
        }
    }
}