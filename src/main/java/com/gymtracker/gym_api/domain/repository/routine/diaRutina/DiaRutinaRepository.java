package com.gymtracker.gym_api.domain.repository.routine.diaRutina;

import com.gymtracker.gym_api.domain.enums.DiaSemana;
import com.gymtracker.gym_api.domain.model.routine.diaRutina.DiaRutina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiaRutinaRepository {

    DiaRutina save(DiaRutina request);

    List<DiaRutina> obtenerPorRutinaId(UUID usuarioId);

    Optional<DiaRutina> obtenerPorIdYRutinaId(UUID id, UUID rutinaId);

    void delete(DiaRutina diaRutina);

    boolean existePorRutinaIdYDiaSemana(UUID rutinaId, DiaSemana diaSemana);

    int contarDiasPorRutinaId(UUID rutinaId);

    boolean existePorRutinaIdYOrdenDia(UUID rutinaId, Integer ordenDia);
}
