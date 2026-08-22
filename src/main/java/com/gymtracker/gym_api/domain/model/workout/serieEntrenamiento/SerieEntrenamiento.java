package com.gymtracker.gym_api.domain.model.workout.serieEntrenamiento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class SerieEntrenamiento {

    private UUID id;

    private UUID sesionEntrenamientoId;

    private UUID serieEjercicioRutinaId;

    private Integer repeticionesRealizadas;

    private BigDecimal pesoUtilizado;
}
