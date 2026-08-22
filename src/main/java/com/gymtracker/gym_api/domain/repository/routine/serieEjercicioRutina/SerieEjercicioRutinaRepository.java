package com.gymtracker.gym_api.domain.repository.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.domain.model.routine.serieEjercicioRutina.SerieEjercicioRutina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SerieEjercicioRutinaRepository {

    SerieEjercicioRutina save(SerieEjercicioRutina serieEjercicioRutina);

    Optional<SerieEjercicioRutina> obtenerActivaPorId(UUID serieEjercicioRutinaId);

    List<SerieEjercicioRutina> obtenerActivasPorEjercicioRutinaId(UUID ejercicioRutinaId);

    Optional<SerieEjercicioRutina> obtenerActivaPorIdYEjercicioRutinaId(
            UUID serieEjercicioRutinaId,
            UUID ejercicioRutinaId
    );

    boolean existeActivaPorEjercicioRutinaIdYOrden(
            UUID ejercicioRutinaId,
            int orden
    );

    int contarActivasPorEjercicioRutinaId(UUID ejercicioRutinaId);

    boolean perteneceActivaARutina(UUID serieEjercicioRutinaId, UUID rutinaId);
}
