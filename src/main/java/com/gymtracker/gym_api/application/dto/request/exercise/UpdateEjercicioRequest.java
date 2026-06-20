package com.gymtracker.gym_api.application.dto.request.exercise;

import com.gymtracker.gym_api.domain.enums.GrupoMuscular;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateEjercicioRequest {
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    private GrupoMuscular grupoMuscular;

    @Size(max = 500, message = "La descripción debe ser menor o igual a 500 caracteres")
    private String descripcion;
}
