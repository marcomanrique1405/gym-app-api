package com.gymtracker.gym_api.application.dto.response.workout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SesionEntrenamientoResponse {

    private UUID id;
    private UUID usuarioId;
    private UUID rutinaId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean finalizada;

}
