package com.gymtracker.gym_api.application.dto.request.routine;

import com.gymtracker.gym_api.domain.enums.TipoProgresion;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateRutinaRequest {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    private TipoProgresion tipoProgresion;

}
