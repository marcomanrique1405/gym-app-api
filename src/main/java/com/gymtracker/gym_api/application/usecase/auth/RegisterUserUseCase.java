package com.gymtracker.gym_api.application.usecase.auth;

import com.gymtracker.gym_api.application.dto.request.auth.RegisterUserRequest;
import com.gymtracker.gym_api.application.dto.response.auth.AuthResponse;
import com.gymtracker.gym_api.domain.model.auth.Usuario;
import com.gymtracker.gym_api.domain.repository.auth.UsuarioRepository;
import com.gymtracker.gym_api.shared.exception.auth.EmailAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class RegisterUserUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public AuthResponse register(RegisterUserRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        String passwordEncriptada = passwordEncoder.encode(request.getPassword());

        if (usuarioRepository.existePorEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                request.getNombre(),
                email,
                passwordEncriptada,
                LocalDateTime.now());

        Usuario usuarioGuardado = usuarioRepository.guardar(usuario);

        return toResponse(usuarioGuardado);

    }

    private AuthResponse toResponse(Usuario usuario) {
        return new AuthResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getFechaRegistro()
                );
    }

}
