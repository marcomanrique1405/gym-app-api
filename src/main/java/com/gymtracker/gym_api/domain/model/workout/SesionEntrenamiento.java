package com.gymtracker.gym_api.domain.model.workout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class SesionEntrenamiento {

    private UUID id;
    private UUID usuarioId;
    private UUID rutinaId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean finalizada;

}
