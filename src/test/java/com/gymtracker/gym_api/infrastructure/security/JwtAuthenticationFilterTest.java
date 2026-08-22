package com.gymtracker.gym_api.infrastructure.security;

import com.gymtracker.gym_api.domain.enums.Rol;
import com.gymtracker.gym_api.domain.model.auth.Usuario;
import com.gymtracker.gym_api.domain.repository.auth.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {
    private JwtService jwtService;
    private UsuarioRepository usuarioRepository;
    private JwtAuthenticationFilter filter;
    private UUID userId;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        usuarioRepository = mock(UsuarioRepository.class);
        JsonMapper mapper = JsonMapper.builder().build();
        RestAuthenticationEntryPoint entryPoint =
                new RestAuthenticationEntryPoint(new SecurityErrorWriter(mapper));
        filter = new JwtAuthenticationFilter(jwtService, usuarioRepository, entryPoint);
        userId = UUID.randomUUID();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void tokenValidoAutenticaUsuarioActivoExistente() throws Exception {
        prepararToken("USER");
        when(usuarioRepository.buscarPorId(userId)).thenReturn(Optional.of(usuario(Rol.USER, true)));
        MockHttpServletResponse response = ejecutar();
        assertEquals(200, response.getStatus());
        assertEquals(userId.toString(), SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void tokenInvalidoDevuelveApiError401() throws Exception {
        when(jwtService.isTokenValid("token")).thenReturn(false);
        MockHttpServletResponse response = ejecutar();
        assertApiError401(response);
    }

    @Test
    void loginPublicoIgnoraTokenInvalido() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/auth/login");
        request.setServletPath("/auth/login");
        request.addHeader("Authorization", "Bearer token-invalido");

        assertTrue(filter.shouldNotFilter(request));
        verifyNoInteractions(jwtService, usuarioRepository);
    }

    @Test
    void rolDesconocidoDevuelve401() throws Exception {
        prepararToken("OWNER");
        assertApiError401(ejecutar());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void tokenSinSubjectDevuelve401() throws Exception {
        when(jwtService.isTokenValid("token")).thenReturn(true);
        when(jwtService.extractUserId("token")).thenReturn(null);

        assertApiError401(ejecutar());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void tokenSinRoleDevuelve401() throws Exception {
        when(jwtService.isTokenValid("token")).thenReturn(true);
        when(jwtService.extractUserId("token")).thenReturn(userId.toString());
        when(jwtService.extractRoleName("token")).thenReturn(null);

        assertApiError401(ejecutar());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void usuarioInexistenteDevuelve401() throws Exception {
        prepararToken("USER");
        when(usuarioRepository.buscarPorId(userId)).thenReturn(Optional.empty());
        assertApiError401(ejecutar());
    }

    @Test
    void usuarioInactivoDevuelve401() throws Exception {
        prepararToken("USER");
        when(usuarioRepository.buscarPorId(userId)).thenReturn(Optional.of(usuario(Rol.USER, false)));
        assertApiError401(ejecutar());
    }

    @Test
    void rolPersistidoDistintoDelTokenDevuelve401() throws Exception {
        prepararToken("ADMIN");
        when(usuarioRepository.buscarPorId(userId)).thenReturn(Optional.of(usuario(Rol.USER, true)));
        assertApiError401(ejecutar());
    }

    private void prepararToken(String role) {
        when(jwtService.isTokenValid("token")).thenReturn(true);
        when(jwtService.extractUserId("token")).thenReturn(userId.toString());
        when(jwtService.extractRoleName("token")).thenReturn(role);
    }

    private MockHttpServletResponse ejecutar() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/rutinas");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilterInternal(request, response, new MockFilterChain());
        return response;
    }

    private Usuario usuario(Rol role, boolean active) {
        return Usuario.builder().id(userId).nombre("Usuario").email("u@example.com")
                .password("hash").rol(role).activo(active).build();
    }

    private void assertApiError401(MockHttpServletResponse response) throws Exception {
        assertEquals(401, response.getStatus());
        String json = response.getContentAsString();
        assertTrue(json.contains("\"status\":401"));
        assertTrue(json.contains("\"error\":\"Unauthorized\""));
        assertTrue(json.contains("\"path\":\"/rutinas\""));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
