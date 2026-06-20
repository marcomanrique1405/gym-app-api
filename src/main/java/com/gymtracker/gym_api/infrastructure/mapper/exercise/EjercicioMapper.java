package com.gymtracker.gym_api.infrastructure.mapper.exercise;

import com.gymtracker.gym_api.domain.model.exercise.Ejercicio;
import com.gymtracker.gym_api.infrastructure.entity.exercise.EjercicioEntity;
import org.springframework.stereotype.Component;

@Component
public class EjercicioMapper {

    public EjercicioEntity toEntity(Ejercicio ejercicio) {
        return new EjercicioEntity(
                ejercicio.getId(),
                ejercicio.getNombre(),
                ejercicio.getGrupoMuscular(),
                ejercicio.getDescripcion(),
                ejercicio.getActivo()
        );
    }

    public Ejercicio toDomain(EjercicioEntity entity) {
        return Ejercicio.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .grupoMuscular(entity.getGrupoMuscular())
                .descripcion(entity.getDescripcion())
                .activo(entity.getActivo())
                .build();
    }

}
