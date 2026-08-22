package com.gymtracker.gym_api.domain.model.routine.ejercicioRutina;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class EjercicioRutina {

    private UUID id;

    private UUID diaRutinaId;

    private UUID ejercicioId;

    private int orden;

    private BigDecimal pesoObjetivo;

    private BigDecimal incrementoPeso;

    private boolean sobrecargaActiva;

    private boolean activo;

}
