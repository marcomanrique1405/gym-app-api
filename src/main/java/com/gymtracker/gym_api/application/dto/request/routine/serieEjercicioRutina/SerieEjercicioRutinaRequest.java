package com.gymtracker.gym_api.application.dto.request.routine.serieEjercicioRutina;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerieEjercicioRutinaRequest {

    @NotNull(message = "El orden es obligatorio")
    @Min(value = 1, message = "El orden debe ser mayor o igual a 1")
    private Integer orden;

    @NotNull(message = "Las repeticiones mínimas son obligatorias")
    @Min(value = 1, message = "Las repeticiones mínimas deben ser mayores o iguales a 1")
    private Integer repeticionesMin;

    @NotNull(message = "Las repeticiones máximas son obligatorias")
    @Min(value = 1, message = "Las repeticiones máximas deben ser mayores o iguales a 1")
    private Integer repeticionesMax;

    @AssertTrue(message = "Las repeticiones mínimas no pueden ser mayores que las máximas")
    public boolean isRangoRepeticionesValido() {
        return repeticionesMin == null
                || repeticionesMax == null
                || repeticionesMin <= repeticionesMax;
    }
}
