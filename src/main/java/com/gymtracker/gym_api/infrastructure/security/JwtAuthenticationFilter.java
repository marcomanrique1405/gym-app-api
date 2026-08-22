package com.gymtracker.gym_api.infrastructure.security;

import com.gymtracker.gym_api.domain.enums.Rol;
import io.jsonwebtoken.JwtException;
import com.gymtracker.gym_api.domain.model.auth.Usuario;
import com.gymtracker.gym_api.domain.repository.auth.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioRepository usuarioRepository,
                                   RestAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/auth/login".equals(request.getServletPath())
                || "/auth/register".equals(request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtService.isTokenValid(token)) throw new IllegalArgumentException("Token inválido");
            String subject = requiredClaim(jwtService.extractUserId(token), "sub");
            String role = requiredClaim(jwtService.extractRoleName(token), "role");
            UUID userId = UUID.fromString(subject);
            Rol tokenRole = Rol.valueOf(role);
            Usuario usuario = usuarioRepository.buscarPorId(userId)
                    .filter(user -> Boolean.TRUE.equals(user.getActivo()))
                    .orElseThrow(() -> new IllegalArgumentException("Usuario inválido"));
            if (usuario.getRol() != tokenRole) throw new IllegalArgumentException("Rol inválido");
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId.toString(), null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())));
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException | IllegalArgumentException exception) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, null);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String requiredClaim(String value, String claimName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Claim JWT ausente: " + claimName);
        }
        return value;
    }
}
