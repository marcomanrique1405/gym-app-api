package com.gymtracker.gym_api.domain.repository.workout;

import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.model.PageResult;

import java.util.Optional;
import java.util.UUID;

public interface SesionEntrenamientoRepository {

    SesionEntrenamiento save(SesionEntrenamiento sesionEntrenamiento);

    Optional<SesionEntrenamiento> obtenerPorIdYUsuarioId(UUID sesionEntrenamientoId, UUID usuarioId);

    Optional<SesionEntrenamiento> obtenerNoFinalizadaPorUsuarioId(UUID usuarioId);

    PageResult<SesionEntrenamiento> obtenerPorUsuarioId(UUID usuarioId, int page, int size);

}
