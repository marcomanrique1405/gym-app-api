package com.gymtracker.gym_api.application.dto.response.auth;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseRegister {
    private UUID id;
    private String nombre;
    private String email;
    private LocalDateTime fechaRegistro;
}
