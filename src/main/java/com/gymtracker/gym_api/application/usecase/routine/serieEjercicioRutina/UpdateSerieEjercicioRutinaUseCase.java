package com.gymtracker.gym_api.application.usecase.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.application.dto.request.routine.serieEjercicioRutina.UpdateSerieEjercicioRutinaRequest;
import com.gymtracker.gym_api.application.dto.response.routine.serieEjercicioRutina.SerieEjercicioRutinaResponse;
import com.gymtracker.gym_api.domain.model.routine.diaRutina.DiaRutina;
import com.gymtracker.gym_api.domain.model.routine.ejercicioRutina.EjercicioRutina;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.model.routine.serieEjercicioRutina.SerieEjercicioRutina;
import com.gymtracker.gym_api.domain.repository.routine.diaRutina.DiaRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.ejercicioRutina.EjercicioRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.serieEjercicioRutina.SerieEjercicioRutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.diaRutina.DiaRutinaNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.ejercicioRutina.EjercicioRutinaNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina.SerieEjercicioRutinaAlreadyExistsException;
import com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina.SerieEjercicioRutinaInvalidRepetitionsException;
import com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina.SerieEjercicioRutinaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateSerieEjercicioRutinaUseCase {

    private final SerieEjercicioRutinaRepository serieRepository;
    private final RutinaRepository rutinaRepository;
    private final DiaRutinaRepository diaRutinaRepository;
    private final EjercicioRutinaRepository ejercicioRutinaRepository;
    private final SecurityUtils securityUtils;

    public UpdateSerieEjercicioRutinaUseCase(SerieEjercicioRutinaRepository serieRepository,
                                             RutinaRepository rutinaRepository,
                                             DiaRutinaRepository diaRutinaRepository,
                                             EjercicioRutinaRepository ejercicioRutinaRepository,
                                             SecurityUtils securityUtils) {
        this.serieRepository = serieRepository;
        this.rutinaRepository = rutinaRepository;
        this.diaRutinaRepository = diaRutinaRepository;
        this.ejercicioRutinaRepository = ejercicioRutinaRepository;
        this.securityUtils = securityUtils;
    }

    public SerieEjercicioRutinaResponse update(UUID rutinaId,
                                               UUID diaRutinaId,
                                               UUID ejercicioRutinaId,
                                               UUID serieEjercicioRutinaId,
                                               UpdateSerieEjercicioRutinaRequest request) {
        EjercicioRutina ejercicioRutina = validarJerarquia(rutinaId, diaRutinaId, ejercicioRutinaId);

        SerieEjercicioRutina actual = serieRepository
                .obtenerActivaPorIdYEjercicioRutinaId(serieEjercicioRutinaId, ejercicioRutina.getId())
                .orElseThrow(SerieEjercicioRutinaNotFoundException::new);

        int ordenFinal = request.getOrden() != null ? request.getOrden() : actual.getOrden();
        int repeticionesMinFinal = request.getRepeticionesMin() != null
                ? request.getRepeticionesMin() : actual.getRepeticionesMin();
        int repeticionesMaxFinal = request.getRepeticionesMax() != null
                ? request.getRepeticionesMax() : actual.getRepeticionesMax();

        if (repeticionesMinFinal > repeticionesMaxFinal) {
            throw new SerieEjercicioRutinaInvalidRepetitionsException();
        }

        if (ordenFinal != actual.getOrden()
                && serieRepository.existeActivaPorEjercicioRutinaIdYOrden(ejercicioRutina.getId(), ordenFinal)) {
            throw new SerieEjercicioRutinaAlreadyExistsException();
        }

        SerieEjercicioRutina actualizada = SerieEjercicioRutina.builder()
                .id(actual.getId())
                .ejercicioRutinaId(actual.getEjercicioRutinaId())
                .orden(ordenFinal)
                .repeticionesMin(repeticionesMinFinal)
                .repeticionesMax(repeticionesMaxFinal)
                .activo(actual.isActivo())
                .build();

        SerieEjercicioRutina guardada = serieRepository.save(actualizada);
        return SerieEjercicioRutinaResponse.builder()
                .id(guardada.getId())
                .ejercicioRutinaId(guardada.getEjercicioRutinaId())
                .orden(guardada.getOrden())
                .repeticionesMin(guardada.getRepeticionesMin())
                .repeticionesMax(guardada.getRepeticionesMax())
                .activo(guardada.isActivo())
                .build();
    }

    private EjercicioRutina validarJerarquia(UUID rutinaId, UUID diaRutinaId, UUID ejercicioRutinaId) {
        UUID usuarioId = securityUtils.getCurrentUserId();
        Rutina rutina = rutinaRepository.obtenerPorIdYUsuarioId(rutinaId, usuarioId)
                .orElseThrow(RoutineNotFoundException::new);

        if (Boolean.FALSE.equals(rutina.getActiva())) {
            throw new RoutineNotFoundException();
        }

        DiaRutina diaRutina = diaRutinaRepository.obtenerPorIdYRutinaId(diaRutinaId, rutina.getId())
                .orElseThrow(DiaRutinaNotFoundException::new);

        return ejercicioRutinaRepository.obtenerActivoPorIdYDiaRutinaId(ejercicioRutinaId, diaRutina.getId())
                .orElseThrow(EjercicioRutinaNotFoundException::new);
    }
}
