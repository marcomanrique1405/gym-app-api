package com.gymtracker.gym_api.domain.model.routine.rutina;

import com.gymtracker.gym_api.domain.enums.TipoProgresion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Rutina {

    private UUID id;
    private UUID usuarioId;
    private String nombre;
    private TipoProgresion tipoProgresion;
    private Boolean activa;
    private LocalDateTime fechaCreacion;

}
