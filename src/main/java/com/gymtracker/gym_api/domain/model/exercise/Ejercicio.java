package com.gymtracker.gym_api.domain.model.exercise;

import com.gymtracker.gym_api.domain.enums.GrupoMuscular;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Ejercicio {

    private UUID id;
    private String nombre;
    private GrupoMuscular grupoMuscular;
    private String descripcion;
    private Boolean activo;

}
