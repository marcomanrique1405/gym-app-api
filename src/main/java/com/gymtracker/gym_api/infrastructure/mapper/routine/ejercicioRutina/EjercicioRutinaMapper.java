package com.gymtracker.gym_api.infrastructure.mapper.routine.ejercicioRutina;

import com.gymtracker.gym_api.domain.model.routine.ejercicioRutina.EjercicioRutina;
import com.gymtracker.gym_api.infrastructure.entity.routine.ejercicioRutina.EjercicioRutinaEntity;
import org.springframework.stereotype.Component;

@Component
public class EjercicioRutinaMapper {

    public EjercicioRutinaEntity toEntity(EjercicioRutina ejercicioRutina) {
        return new EjercicioRutinaEntity(
                ejercicioRutina.getId(),
                ejercicioRutina.getDiaRutinaId(),
                ejercicioRutina.getEjercicioId(),
                ejercicioRutina.getOrden(),
                ejercicioRutina.getPesoObjetivo(),
                ejercicioRutina.getIncrementoPeso(),
                ejercicioRutina.isSobrecargaActiva(),
                ejercicioRutina.isActivo()
        );
    }

    public EjercicioRutina toDomain(EjercicioRutinaEntity entity) {
        return EjercicioRutina.builder()
                .id(entity.getId())
                .diaRutinaId(entity.getDiaRutinaId())
                .ejercicioId(entity.getEjercicioId())
                .orden(entity.getOrden())
                .pesoObjetivo(entity.getPesoObjetivo())
                .incrementoPeso(entity.getIncrementoPeso())
                .sobrecargaActiva(entity.isSobrecargaActiva())
                .activo(entity.isActivo())
                .build();
    }
}