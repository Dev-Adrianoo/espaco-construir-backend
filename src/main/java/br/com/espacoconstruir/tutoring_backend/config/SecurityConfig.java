package br.com.espacoconstruir.tutoring_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import br.com.espacoconstruir.tutoring_backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import java.util.Arrays;

@Configuration
public class SecurityConfig {

  @Autowired
  private JwtAuthenticationFilter jwtAuthFilter;

  @Value("${cors.allowed-origins}")
  private String allowedOrigins;

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "accept",
        "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
    configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth

            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            // 1. Rotas Públicas
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/guardians/register").permitAll()
            .requestMatchers("/teachers/register").permitAll()

            // 2. Regras Específicas de PROFESSORA
            .requestMatchers("/students/teacher/**").hasAuthority("PROFESSORA")
            
            // 3. Regras Específicas de RESPONSAVEL
            .requestMatchers("/guardians/children/**").hasAuthority("RESPONSAVEL")

            // 4. Regras para /students (agora explícitas e corretas)
            .requestMatchers(HttpMethod.POST, "/students/register").hasAnyAuthority("RESPONSAVEL", "PROFESSORA")
            .requestMatchers(HttpMethod.PUT, "/students/**").hasAnyAuthority("RESPONSAVEL", "PROFESSORA")
            .requestMatchers(HttpMethod.GET, "/students/**").hasAnyAuthority("RESPONSAVEL", "PROFESSORA")
            .requestMatchers(HttpMethod.DELETE, "/students/**").hasAnyAuthority("RESPONSAVEL", "PROFESSORA")

            // 5. Outras regras compartilhadas
            .requestMatchers("/schedules/**").hasAnyAuthority("RESPONSAVEL", "PROFESSORA")
            .requestMatchers("/history/**").hasAnyAuthority("RESPONSAVEL", "PROFESSORA")
            .requestMatchers("/teachers").hasAnyAuthority("RESPONSAVEL", "PROFESSORA")
            
            // 6. Qualquer outra rota precisa estar autenticada
            .anyRequest().permitAll()
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
}