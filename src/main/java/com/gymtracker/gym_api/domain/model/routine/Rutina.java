package com.gymtracker.gym_api.domain.model.routine;

import com.gymtracker.gym_api.domain.enums.TipoProgresion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class Rutina {

    private UUID id;
    private UUID usuarioId;
    private String nombre;
    private TipoProgresion tipoProgresion;
    private Boolean activa;
    private LocalDateTime fechaCreacion;

}
