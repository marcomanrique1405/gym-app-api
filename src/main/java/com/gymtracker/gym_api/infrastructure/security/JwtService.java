package com.gymtracker.gym_api.infrastructure.security;

import com.gymtracker.gym_api.domain.enums.Rol;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationTime;

    public JwtService(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") long expirationTime
    ) {
        this.signingKey = createSigningKey(secretKey);
        if (expirationTime <= 0) {
            throw new IllegalStateException("jwt.expiration debe ser mayor que cero");
        }
        this.expirationTime = expirationTime;
    }

    public String generateToken(String userId, String email, Rol rol) {
        return Jwts.builder()
                .subject(userId)
                .claim("email", email)
                .claim("role", rol.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(signingKey)
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRoleName(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static SecretKey createSigningKey(String encodedSecret) {
        if (encodedSecret == null || encodedSecret.isBlank()) {
            throw new IllegalStateException("jwt.secret es obligatorio");
        }

        try {
            byte[] keyBytes = Decoders.BASE64.decode(encodedSecret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (RuntimeException ex) {
            throw new IllegalStateException(
                    "jwt.secret debe ser Base64 válido y representar al menos 32 bytes",
                    ex
            );
        }
    }
}
