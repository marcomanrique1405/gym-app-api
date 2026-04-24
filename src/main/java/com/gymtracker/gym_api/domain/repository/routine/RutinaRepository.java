package com.gymtracker.gym_api.domain.repository.routine;

import com.gymtracker.gym_api.domain.model.routine.Rutina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RutinaRepository {

    Rutina save(Rutina rutina);

    boolean existePorUsuarioIdYNombre(UUID usuarioId, String nombre);

    List<Rutina> obtenerPorUsuarioIdYActivaTrue(UUID usuarioId);

    Optional<Rutina> obtenerPorId(UUID id);

    boolean existePorId(UUID id);

    Optional<Rutina> obtenerPorIdYUsuarioId(UUID id, UUID usuarioId);
}
