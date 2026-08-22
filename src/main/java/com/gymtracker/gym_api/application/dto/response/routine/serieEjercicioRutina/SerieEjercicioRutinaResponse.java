package com.gymtracker.gym_api.application.dto.response.routine.serieEjercicioRutina;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class SerieEjercicioRutinaResponse {

    private UUID id;
    private UUID ejercicioRutinaId;
    private Integer orden;
    private Integer repeticionesMin;
    private Integer repeticionesMax;
    private Boolean activo;
}
