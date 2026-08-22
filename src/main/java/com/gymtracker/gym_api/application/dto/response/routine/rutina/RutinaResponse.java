package com.gymtracker.gym_api.application.dto.response.routine.rutina;

import com.gymtracker.gym_api.domain.enums.TipoProgresion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RutinaResponse {
    private UUID id;
    private String nombre;
    private TipoProgresion tipoProgresion;
}