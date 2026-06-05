package com.gymtracker.gym_api.domain.model.routine.diaRutina;

import com.gymtracker.gym_api.domain.enums.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
@Getter
@Builder
public class DiaRutina {

    private UUID id;
    private UUID rutinaId;
    private DiaSemana diaSemana;
    private int ordenDia;

    public void actualizarDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

}
