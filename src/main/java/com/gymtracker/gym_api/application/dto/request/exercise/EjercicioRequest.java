package com.gymtracker.gym_api.application.dto.request.exercise;

import com.gymtracker.gym_api.domain.enums.GrupoMuscular;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EjercicioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotNull(message = "El grupo muscular es obligatorio")
    private GrupoMuscular grupoMuscular;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción debe ser menor o igual a 500 caracteres")
    private String descripcion;
}