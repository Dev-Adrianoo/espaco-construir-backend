package br.com.espacoconstruir.tutoring_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import br.com.espacoconstruir.tutoring_backend.service.UserService;
import br.com.espacoconstruir.tutoring_backend.service.JwtService;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtService jwtService;

  @Lazy
  @Autowired
  private UserService userService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

       
      final String requestURI = request.getRequestURI();

      if (requestURI.startsWith("/auth/") || requestURI.equals("/guardians/register") || requestURI.equals("/teachers/register")) {
        filterChain.doFilter(request, response);
        return;
      }

    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;

    System.out.println("Entering JwtAuthenticationFilter for request: " + request.getRequestURI());
    System.out.println("[DEBUG] Authorization header: " + authHeader + " | Endpoint: " + request.getRequestURI());

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      System.out.println("No JWT found or invalid format. Proceeding with filter chain.");
      filterChain.doFilter(request, response);
      return;
    }

    jwt = authHeader.substring(7);
    userEmail = jwtService.extractUsername(jwt);

    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userService.loadUserByUsername(userEmail);

      if (jwtService.isTokenValid(jwt, userDetails)) {
        System.out.println("JWT is valid. Setting authentication context.");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
          System.out.println("[JWT DEBUG] Usuário autenticado: " + authentication.getName());
          System.out.println("[JWT DEBUG] Authorities: ");
          for (GrantedAuthority authority : authentication.getAuthorities()) {
            System.out.println("[JWT DEBUG] - " + authority.getAuthority());
          }
          System.out.println("[DEBUG] Authorities no contexto: " + authentication.getAuthorities());
        }
      } else {
        System.out.println("JWT is NOT valid for user: " + userEmail);
      }
    } else {
      System.out.println("User email is null or authentication already set.");
    }

    filterChain.doFilter(request, response);
  }
}