package com.gymtracker.gym_api.application.usecase.auth;

import com.gymtracker.gym_api.application.dto.request.auth.LoginRequest;
import com.gymtracker.gym_api.application.dto.request.auth.RegisterUserRequest;
import com.gymtracker.gym_api.domain.enums.Rol;
import com.gymtracker.gym_api.domain.model.auth.Usuario;
import com.gymtracker.gym_api.domain.repository.auth.UsuarioRepository;
import com.gymtracker.gym_api.infrastructure.security.JwtService;
import com.gymtracker.gym_api.shared.exception.auth.EmailAlreadyExistsException;
import com.gymtracker.gym_api.shared.exception.auth.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUseCasesTest {
    @Mock UsuarioRepository repository;
    @Mock PasswordEncoder encoder;
    @Mock JwtService jwtService;
    RegisterUserUseCase register;
    LoginUserUseCase login;

    @BeforeEach void setUp() {
        register = new RegisterUserUseCase(repository, encoder);
        login = new LoginUserUseCase(repository, encoder, jwtService);
    }

    @Test void registroSiempreCreaUserYNormalizaEmail() {
        RegisterUserRequest request = registerRequest();
        when(encoder.encode("password123")).thenReturn("hash");
        when(repository.guardar(any())).thenAnswer(invocation -> invocation.getArgument(0));
        register.register(request);
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).guardar(captor.capture());
        assertEquals(Rol.USER, captor.getValue().getRol());
        assertEquals("user@example.com", captor.getValue().getEmail());
        assertTrue(captor.getValue().getActivo());
    }

    @Test void registroDuplicadoEsConflicto() {
        when(repository.existePorEmail("user@example.com")).thenReturn(true);
        assertThrows(EmailAlreadyExistsException.class, () -> register.register(registerRequest()));
        verify(repository, never()).guardar(any());
    }

    @Test void loginCorrectoGeneraToken() {
        Usuario user = user(true);
        when(repository.buscarPorEmail("user@example.com")).thenReturn(Optional.of(user));
        when(encoder.matches("password123", "hash")).thenReturn(true);
        when(jwtService.generateToken(any(), any(), any())).thenReturn("jwt");
        assertEquals("jwt", login.login(loginRequest()).getToken());
    }

    @Test void loginRechazaPasswordIncorrecta() {
        when(repository.buscarPorEmail("user@example.com")).thenReturn(Optional.of(user(true)));
        when(encoder.matches(any(), any())).thenReturn(false);
        assertThrows(InvalidCredentialsException.class, () -> login.login(loginRequest()));
    }

    @Test void loginRechazaUsuarioInactivo() {
        when(repository.buscarPorEmail("user@example.com")).thenReturn(Optional.of(user(false)));
        when(encoder.matches(any(), any())).thenReturn(true);
        assertThrows(InvalidCredentialsException.class, () -> login.login(loginRequest()));
        verify(jwtService, never()).generateToken(any(), any(), any());
    }

    private RegisterUserRequest registerRequest() {
        RegisterUserRequest r = new RegisterUserRequest(); r.setNombre("User");
        r.setEmail(" User@Example.com "); r.setPassword("password123"); return r;
    }
    private LoginRequest loginRequest() {
        LoginRequest r = new LoginRequest(); r.setEmail("USER@example.com"); r.setPassword("password123"); return r;
    }
    private Usuario user(boolean active) {
        return Usuario.builder().id(UUID.randomUUID()).nombre("User").email("user@example.com")
                .password("hash").fechaRegistro(LocalDateTime.now()).rol(Rol.USER).activo(active).build();
    }
}
