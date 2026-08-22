package com.gymtracker.gym_api.application.dto.response.routine.ejercicioRutina;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class EjercicioRutinaResponse {

    private UUID id;

    private UUID diaRutinaId;

    private UUID ejercicioId;

    private Integer orden;

    private BigDecimal pesoObjetivo;

    private BigDecimal incrementoPeso;

    private Boolean sobrecargaActiva;

    private Boolean activo;
}