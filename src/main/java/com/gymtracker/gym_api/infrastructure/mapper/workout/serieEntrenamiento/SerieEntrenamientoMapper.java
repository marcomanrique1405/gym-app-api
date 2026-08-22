package com.gymtracker.gym_api.infrastructure.mapper.workout.serieEntrenamiento;

import com.gymtracker.gym_api.domain.model.workout.serieEntrenamiento.SerieEntrenamiento;
import com.gymtracker.gym_api.infrastructure.entity.workout.serieEntrenamiento.SerieEntrenamientoEntity;
import org.springframework.stereotype.Component;

@Component
public class SerieEntrenamientoMapper {

    public SerieEntrenamientoEntity toEntity(SerieEntrenamiento serieEntrenamiento) {
        return new SerieEntrenamientoEntity(
                serieEntrenamiento.getId(),
                serieEntrenamiento.getSesionEntrenamientoId(),
                serieEntrenamiento.getSerieEjercicioRutinaId(),
                serieEntrenamiento.getRepeticionesRealizadas(),
                serieEntrenamiento.getPesoUtilizado()
        );
    }

    public SerieEntrenamiento toDomain(SerieEntrenamientoEntity entity) {
        return SerieEntrenamiento.builder()
                .id(entity.getId())
                .sesionEntrenamientoId(entity.getSesionEntrenamientoId())
                .serieEjercicioRutinaId(entity.getSerieEjercicioRutinaId())
                .repeticionesRealizadas(entity.getRepeticionesRealizadas())
                .pesoUtilizado(entity.getPesoUtilizado())
                .build();
    }
}
