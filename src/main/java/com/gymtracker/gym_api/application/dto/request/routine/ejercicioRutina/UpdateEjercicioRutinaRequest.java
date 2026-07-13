package com.gymtracker.gym_api.application.dto.request.routine.ejercicioRutina;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEjercicioRutinaRequest {

    private UUID diaRutinaId;

    private UUID ejercicioId;

    @Min(value = 1, message = "El orden debe ser mayor o igual a 1")
    private Integer orden;

    @DecimalMin(value = "0.0", inclusive = true, message = "El peso objetivo no puede ser negativo")
    @Digits(integer = 4, fraction = 2, message = "El peso objetivo debe tener máximo 4 dígitos enteros y 2 decimales")
    private BigDecimal pesoObjetivo;

    @DecimalMin(value = "0.0", inclusive = true, message = "El incremento de peso no puede ser negativo")
    @Digits(integer = 3, fraction = 2, message = "El incremento de peso debe tener máximo 3 dígitos enteros y 2 decimales")
    private BigDecimal incrementoPeso;

    private Boolean sobrecargaActiva;
}