package com.gymtracker.gym_api.infrastructure.security;

import com.gymtracker.gym_api.domain.enums.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private static final String KEY = "dGVzdC1vbmx5LWp3dC1rZXktMzItYnl0ZXMtbWluaW11bS1ub3QtcHJvZHVjdGlvbg==";
    private JwtService service;

    @BeforeEach void setUp() {
        service = new JwtService();
        ReflectionTestUtils.setField(service, "secretKey", KEY);
        ReflectionTestUtils.setField(service, "expirationTime", 60_000L);
    }

    @Test void tokenValidoContieneSubjectYRole() {
        String id = UUID.randomUUID().toString();
        String token = service.generateToken(id, "user@example.com", Rol.USER);
        assertTrue(service.isTokenValid(token));
        assertEquals(id, service.extractUserId(token));
        assertEquals("USER", service.extractRoleName(token));
    }

    @Test void tokenAlteradoEsInvalido() {
        String token = service.generateToken(UUID.randomUUID().toString(), "u@e.com", Rol.USER);
        char replacement = token.charAt(token.length() - 1) == 'a' ? 'b' : 'a';
        assertFalse(service.isTokenValid(token.substring(0, token.length() - 1) + replacement));
    }

    @Test void tokenExpiradoEsInvalido() throws InterruptedException {
        ReflectionTestUtils.setField(service, "expirationTime", 1L);
        String token = service.generateToken(UUID.randomUUID().toString(), "u@e.com", Rol.USER);
        Thread.sleep(5L);
        assertFalse(service.isTokenValid(token));
    }
}
