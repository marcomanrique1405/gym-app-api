package com.gymtracker.gym_api.application.dto.request.workout.serieEntrenamiento;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSerieEntrenamientoRequest {

    @Min(value = 1, message = "Las repeticiones realizadas deben ser mayores o iguales a 1")
    private Integer repeticionesRealizadas;

    @DecimalMin(value = "0.0", inclusive = true, message = "El peso utilizado no puede ser negativo")
    private BigDecimal pesoUtilizado;
}
