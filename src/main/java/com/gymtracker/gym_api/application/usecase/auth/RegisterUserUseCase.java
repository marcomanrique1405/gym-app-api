package com.gymtracker.gym_api.application.usecase.auth;

import com.gymtracker.gym_api.application.dto.request.auth.RegisterUserRequest;
import com.gymtracker.gym_api.application.dto.response.auth.AuthResponseRegister;
import com.gymtracker.gym_api.domain.enums.Rol;
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


    public AuthResponseRegister register(RegisterUserRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (usuarioRepository.existePorEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        String passwordEncriptada = passwordEncoder.encode(request.getPassword());

        Usuario usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .nombre(request.getNombre().trim())
                .email(email)
                .password(passwordEncriptada)
                .fechaRegistro(LocalDateTime.now())
                .rol(Rol.USER)
                .activo(true)
                .build();

        Usuario usuarioGuardado = usuarioRepository.guardar(usuario);

        return toResponse(usuarioGuardado);

    }

    private AuthResponseRegister toResponse(Usuario usuario) {
        return new AuthResponseRegister(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getFechaRegistro()
                );
    }

}
