package com.gymtracker.gym_api.domain.repository.workout;

import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SesionEntrenamientoRepository {

    SesionEntrenamiento save(SesionEntrenamiento sesionEntrenamiento);

    Optional<SesionEntrenamiento> obtenerPorIdYUsuarioId(UUID sesionEntrenamientoId, UUID usuarioId);

    Optional<SesionEntrenamiento> obtenerNoFinalizadaPorUsuarioId(UUID usuarioId);

    List<SesionEntrenamiento> obtenerPorUsuarioId(UUID usuarioId);

}
