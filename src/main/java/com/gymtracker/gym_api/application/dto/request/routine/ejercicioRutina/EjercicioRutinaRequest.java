package com.gymtracker.gym_api.application.dto.request.routine.ejercicioRutina;

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
public class EjercicioRutinaRequest {

    @NotNull(message = "El día de rutina es obligatorio")
    private UUID diaRutinaId;

    @NotNull(message = "El ejercicio es obligatorio")
    private UUID ejercicioId;

    @NotNull(message = "El orden es obligatorio")
    @Min(value = 1, message = "El orden debe ser mayor o igual a 1")
    private Integer orden;

    @NotNull(message = "El peso objetivo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El peso objetivo no puede ser negativo")
    @Digits(integer = 4, fraction = 2, message = "El peso objetivo debe tener máximo 4 dígitos enteros y 2 decimales")
    private BigDecimal pesoObjetivo;

    @NotNull(message = "El incremento de peso es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El incremento de peso no puede ser negativo")
    @Digits(integer = 2, fraction = 2, message = "El incremento de peso debe tener máximo 2 dígitos enteros y 2 decimales")
    private BigDecimal incrementoPeso;

    @NotNull(message = "El estado de sobrecarga activa es obligatorio")
    private Boolean sobrecargaActiva;
}
