package com.gymtracker.gym_api.application.dto.request.routine.diaRutina;

import com.gymtracker.gym_api.domain.enums.DiaSemana;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateDiaRutinaRequest {

    @NotNull(message = "El día de la semana es obligatorio")
    private DiaSemana diaSemana;

}
