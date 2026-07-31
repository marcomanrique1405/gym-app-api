package com.gymtracker.gym_api.application.dto.request.exercise;

import com.gymtracker.gym_api.domain.enums.GrupoMuscular;
import com.gymtracker.gym_api.shared.validation.NormalizedSize;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateEjercicioRequest {
    @NormalizedSize(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres después de quitar espacios externos")
    private String nombre;

    private GrupoMuscular grupoMuscular;

    @Pattern(regexp = ".*\\S.*", message = "La descripción no puede estar vacía")
    @Size(max = 500, message = "La descripción debe ser menor o igual a 500 caracteres")
    private String descripcion;
}
