package com.gymtracker.gym_api.domain.model.auth;

import com.gymtracker.gym_api.domain.enums.Rol;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class Usuario {
    private UUID id;
    private String nombre;
    private String email;
    private String password;
    private LocalDateTime fechaRegistro;
    private Rol rol;
    private Boolean activo;
}
