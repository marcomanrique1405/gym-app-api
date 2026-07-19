package com.gymtracker.gym_api.infrastructure.mapper.workout;

import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.infrastructure.entity.workout.SesionEntrenamientoEntity;
import org.springframework.stereotype.Component;

@Component
public class SesionEntrenamientoMapper {

    public SesionEntrenamientoEntity toEntity(SesionEntrenamiento sesionEntrenamiento) {
        SesionEntrenamientoEntity entity = new SesionEntrenamientoEntity();

        entity.setId(sesionEntrenamiento.getId());
        entity.setUsuarioId(sesionEntrenamiento.getUsuarioId());
        entity.setRutinaId(sesionEntrenamiento.getRutinaId());
        entity.setFechaInicio(sesionEntrenamiento.getFechaInicio());
        entity.setFechaFin(sesionEntrenamiento.getFechaFin());
        entity.setFinalizada(sesionEntrenamiento.isFinalizada());

        return entity;
    }

    public SesionEntrenamiento toDomain(SesionEntrenamientoEntity entity) {
        return SesionEntrenamiento.builder()
                .id(entity.getId())
                .usuarioId(entity.getUsuarioId())
                .rutinaId(entity.getRutinaId())
                .fechaInicio(entity.getFechaInicio())
                .fechaFin(entity.getFechaFin())
                .finalizada(entity.isFinalizada())
                .build();
    }

}
