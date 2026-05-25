package com.gymtracker.gym_api.application.dto.request.routine.diaRutina;

import com.gymtracker.gym_api.domain.enums.DiaSemana;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaRutinaRequest {

    @NotNull(message = "El día de la semana es obligatorio")
    private DiaSemana diaSemana;

}
