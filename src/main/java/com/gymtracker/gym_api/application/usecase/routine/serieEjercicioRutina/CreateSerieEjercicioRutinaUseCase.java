package com.gymtracker.gym_api.application.usecase.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.application.dto.request.routine.serieEjercicioRutina.SerieEjercicioRutinaRequest;
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
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateSerieEjercicioRutinaUseCase {

    private final SerieEjercicioRutinaRepository serieRepository;
    private final RutinaRepository rutinaRepository;
    private final DiaRutinaRepository diaRutinaRepository;
    private final EjercicioRutinaRepository ejercicioRutinaRepository;
    private final SecurityUtils securityUtils;

    public CreateSerieEjercicioRutinaUseCase(
            SerieEjercicioRutinaRepository serieRepository,
            RutinaRepository rutinaRepository,
            DiaRutinaRepository diaRutinaRepository,
            EjercicioRutinaRepository ejercicioRutinaRepository,
            SecurityUtils securityUtils
    ) {
        this.serieRepository = serieRepository;
        this.rutinaRepository = rutinaRepository;
        this.diaRutinaRepository = diaRutinaRepository;
        this.ejercicioRutinaRepository = ejercicioRutinaRepository;
        this.securityUtils = securityUtils;
    }

    public SerieEjercicioRutinaResponse crear(
            UUID rutinaId,
            UUID diaRutinaId,
            UUID ejercicioRutinaId,
            SerieEjercicioRutinaRequest request
    ) {
        EjercicioRutina ejercicioRutina = validarJerarquia(rutinaId, diaRutinaId, ejercicioRutinaId);

        if (request.getRepeticionesMin() > request.getRepeticionesMax()) {
            throw new SerieEjercicioRutinaInvalidRepetitionsException();
        }

        if (serieRepository.existeActivaPorEjercicioRutinaIdYOrden(ejercicioRutina.getId(), request.getOrden())) {
            throw new SerieEjercicioRutinaAlreadyExistsException();
        }

        SerieEjercicioRutina serie = SerieEjercicioRutina.builder()
                .id(UUID.randomUUID())
                .ejercicioRutinaId(ejercicioRutina.getId())
                .orden(request.getOrden())
                .repeticionesMin(request.getRepeticionesMin())
                .repeticionesMax(request.getRepeticionesMax())
                .activo(true)
                .build();

        return toResponse(serieRepository.save(serie));
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

    private SerieEjercicioRutinaResponse toResponse(SerieEjercicioRutina serie) {
        return SerieEjercicioRutinaResponse.builder()
                .id(serie.getId())
                .ejercicioRutinaId(serie.getEjercicioRutinaId())
                .orden(serie.getOrden())
                .repeticionesMin(serie.getRepeticionesMin())
                .repeticionesMax(serie.getRepeticionesMax())
                .activo(serie.isActivo())
                .build();
    }
}
