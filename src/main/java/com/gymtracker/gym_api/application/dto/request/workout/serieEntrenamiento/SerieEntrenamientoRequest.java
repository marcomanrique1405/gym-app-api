package com.gymtracker.gym_api.application.dto.request.workout.serieEntrenamiento;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerieEntrenamientoRequest {

    @NotNull(message = "La serie de ejercicio de rutina es obligatoria")
    private UUID serieEjercicioRutinaId;

    @NotNull(message = "Las repeticiones realizadas son obligatorias")
    @Min(value = 1, message = "Las repeticiones realizadas deben ser mayores o iguales a 1")
    private Integer repeticionesRealizadas;

    @NotNull(message = "El peso utilizado es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El peso utilizado no puede ser negativo")
    @Digits(integer = 8, fraction = 2, message = "El peso utilizado debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal pesoUtilizado;
}
