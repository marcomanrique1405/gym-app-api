package com.gymtracker.gym_api.domain.repository.routine.ejercicioRutina;

import com.gymtracker.gym_api.domain.model.routine.ejercicioRutina.EjercicioRutina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EjercicioRutinaRepository {

    EjercicioRutina save(EjercicioRutina ejercicioRutina);

    List<EjercicioRutina> obtenerActivosPorDiaRutinaId(UUID diaRutinaId);

    Optional<EjercicioRutina> obtenerActivoPorIdYDiaRutinaId(UUID ejercicioRutinaId, UUID diaRutinaId);

    Optional<EjercicioRutina> obtenerActivoPorId(UUID ejercicioRutinaId);

    boolean existeActivoPorDiaRutinaIdYEjercicioId(UUID diaRutinaId, UUID ejercicioId);

    boolean existeActivoPorDiaRutinaIdYEjercicioIdExcluyendoId(
            UUID diaRutinaId, UUID ejercicioId, UUID ejercicioRutinaId);

    boolean existeActivoPorDiaRutinaIdYOrden(UUID diaRutinaId, int orden);

    int contarActivosPorDiaRutinaId(UUID diaRutinaId);
}
