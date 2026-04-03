package com.gymtracker.gym_api.application.usecase.auth;

import com.gymtracker.gym_api.application.dto.request.auth.LoginRequest;
import com.gymtracker.gym_api.application.dto.response.auth.AuthResponse;
import com.gymtracker.gym_api.domain.model.auth.Usuario;
import com.gymtracker.gym_api.domain.repository.auth.UsuarioRepository;
import com.gymtracker.gym_api.infrastructure.security.JwtService;
import com.gymtracker.gym_api.shared.exception.auth.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUserUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginUserUseCase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        Usuario usuario = usuarioRepository
                .buscarPorEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())){
            throw new InvalidCredentialsException();
        }

        if (!usuario.getActivo()) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(
                usuario.getId().toString(),
                usuario.getEmail(),
                usuario.getRol()
        );

        return toResponse(usuario, token);
    }

    private AuthResponse toResponse(Usuario usuario, String token) {
        return AuthResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .fechaRegistro(usuario.getFechaRegistro())
                .token(token)
                .build();

    }
}
