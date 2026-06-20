package com.gymtracker.gym_api.application.dto.response.exercise;

import com.gymtracker.gym_api.domain.enums.GrupoMuscular;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EjercicioResponse {
    private UUID id;
    private String nombre;
    private GrupoMuscular grupoMuscular;
    private String descripcion;
}
