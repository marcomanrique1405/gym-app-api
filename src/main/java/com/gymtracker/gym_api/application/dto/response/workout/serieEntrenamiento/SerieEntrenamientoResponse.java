package com.gymtracker.gym_api.application.dto.response.workout.serieEntrenamiento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerieEntrenamientoResponse {

    private UUID id;
    private UUID sesionEntrenamientoId;
    private UUID serieEjercicioRutinaId;
    private Integer repeticionesRealizadas;
    private BigDecimal pesoUtilizado;
}
