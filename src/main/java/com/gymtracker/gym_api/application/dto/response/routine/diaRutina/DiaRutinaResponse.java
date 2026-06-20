package com.gymtracker.gym_api.application.dto.response.routine.diaRutina;

import com.gymtracker.gym_api.domain.enums.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaRutinaResponse {

    private UUID id;
    private UUID rutinaId;
    private DiaSemana diaSemana;
    private Integer ordenDia;

}
