package com.gymtracker.gym_api.application.dto.request.routine.rutina;

import com.gymtracker.gym_api.domain.enums.TipoProgresion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateRutinaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotNull(message = "El tipo de progreso es obligatorio")
    private TipoProgresion tipoProgresion;

}
