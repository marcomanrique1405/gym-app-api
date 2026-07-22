package com.gymtracker.gym_api.domain.repository.workout.serieEntrenamiento;

import com.gymtracker.gym_api.domain.model.workout.serieEntrenamiento.SerieEntrenamiento;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SerieEntrenamientoRepository {

    SerieEntrenamiento save(SerieEntrenamiento serieEntrenamiento);

    Optional<SerieEntrenamiento> obtenerPorIdYSesionEntrenamientoId(
            UUID serieEntrenamientoId,
            UUID sesionEntrenamientoId
    );

    List<SerieEntrenamiento> obtenerPorSesionEntrenamientoId(UUID sesionEntrenamientoId);

    boolean existePorSesionEntrenamientoIdYSerieEjercicioRutinaId(
            UUID sesionEntrenamientoId,
            UUID serieEjercicioRutinaId
    );
}
