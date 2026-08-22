package com.gymtracker.gym_api.infrastructure.mapper.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.domain.model.routine.serieEjercicioRutina.SerieEjercicioRutina;
import com.gymtracker.gym_api.infrastructure.entity.routine.serieEjercicioRutina.SerieEjercicioRutinaEntity;
import org.springframework.stereotype.Component;

@Component
public class SerieEjercicioRutinaMapper {

    public SerieEjercicioRutinaEntity toEntity(SerieEjercicioRutina serieEjercicioRutina) {
        return new SerieEjercicioRutinaEntity(
                serieEjercicioRutina.getId(),
                serieEjercicioRutina.getEjercicioRutinaId(),
                serieEjercicioRutina.getOrden(),
                serieEjercicioRutina.getRepeticionesMin(),
                serieEjercicioRutina.getRepeticionesMax(),
                serieEjercicioRutina.isActivo()
        );
    }

    public SerieEjercicioRutina toDomain(SerieEjercicioRutinaEntity entity) {
        return SerieEjercicioRutina.builder()
                .id(entity.getId())
                .ejercicioRutinaId(entity.getEjercicioRutinaId())
                .orden(entity.getOrden())
                .repeticionesMin(entity.getRepeticionesMin())
                .repeticionesMax(entity.getRepeticionesMax())
                .activo(entity.isActivo())
                .build();
    }
}
