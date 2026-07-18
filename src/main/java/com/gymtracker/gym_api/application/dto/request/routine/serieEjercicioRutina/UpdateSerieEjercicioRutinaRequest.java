package com.gymtracker.gym_api.application.dto.request.routine.serieEjercicioRutina;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSerieEjercicioRutinaRequest {

    @Min(value = 1, message = "El orden debe ser mayor o igual a 1")
    private Integer orden;

    @Min(value = 1, message = "Las repeticiones mínimas deben ser mayores o iguales a 1")
    private Integer repeticionesMin;

    @Min(value = 1, message = "Las repeticiones máximas deben ser mayores o iguales a 1")
    private Integer repeticionesMax;

    @AssertTrue(message = "Las repeticiones mínimas no pueden ser mayores que las máximas")
    public boolean isRangoRepeticionesValido() {
        return repeticionesMin == null
                || repeticionesMax == null
                || repeticionesMin <= repeticionesMax;
    }
}
