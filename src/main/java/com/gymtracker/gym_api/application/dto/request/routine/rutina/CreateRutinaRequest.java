package com.gymtracker.gym_api.application.dto.request.routine.rutina;

import com.gymtracker.gym_api.domain.enums.TipoProgresion;
import com.gymtracker.gym_api.shared.validation.NormalizedSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateRutinaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @NormalizedSize(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres después de quitar espacios externos")
    private String nombre;

    @NotNull(message = "El tipo de progreso es obligatorio")
    private TipoProgresion tipoProgresion;

}
