package com.gymtracker.gym_api.application.dto.request.routine.rutina;

import com.gymtracker.gym_api.domain.enums.TipoProgresion;
import com.gymtracker.gym_api.shared.validation.NormalizedSize;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateRutinaRequest {

    @NormalizedSize(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres después de quitar espacios externos")
    private String nombre;

    private TipoProgresion tipoProgresion;

}
