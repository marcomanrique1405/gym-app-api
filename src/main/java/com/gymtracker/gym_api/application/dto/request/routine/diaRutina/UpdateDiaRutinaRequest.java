package com.gymtracker.gym_api.application.dto.request.routine.diaRutina;

import com.gymtracker.gym_api.domain.enums.DiaSemana;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateDiaRutinaRequest {

    private DiaSemana diaSemana;

}
