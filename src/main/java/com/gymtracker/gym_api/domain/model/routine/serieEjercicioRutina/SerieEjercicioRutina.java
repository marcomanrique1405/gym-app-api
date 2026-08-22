package com.gymtracker.gym_api.domain.model.routine.serieEjercicioRutina;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class SerieEjercicioRutina {

    private UUID id;

    private UUID ejercicioRutinaId;

    private int orden;

    private int repeticionesMin;

    private int repeticionesMax;

    private boolean activo;
}
