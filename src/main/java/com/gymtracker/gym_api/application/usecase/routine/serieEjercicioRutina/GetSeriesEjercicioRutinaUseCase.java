package com.gymtracker.gym_api.application.usecase.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.application.dto.response.routine.serieEjercicioRutina.SerieEjercicioRutinaResponse;
import com.gymtracker.gym_api.domain.model.routine.diaRutina.DiaRutina;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.diaRutina.DiaRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.ejercicioRutina.EjercicioRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.serieEjercicioRutina.SerieEjercicioRutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.diaRutina.DiaRutinaNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.ejercicioRutina.EjercicioRutinaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetSeriesEjercicioRutinaUseCase {

    private final SerieEjercicioRutinaRepository serieRepository;
    private final RutinaRepository rutinaRepository;
    private final DiaRutinaRepository diaRutinaRepository;
    private final EjercicioRutinaRepository ejercicioRutinaRepository;
    private final SecurityUtils securityUtils;

    public GetSeriesEjercicioRutinaUseCase(SerieEjercicioRutinaRepository serieRepository,
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

    public List<SerieEjercicioRutinaResponse> obtenerSeriesPorEjercicioRutina(
            UUID rutinaId, UUID diaRutinaId, UUID ejercicioRutinaId
    ) {
        UUID usuarioId = securityUtils.getCurrentUserId();
        Rutina rutina = rutinaRepository.obtenerPorIdYUsuarioId(rutinaId, usuarioId)
                .orElseThrow(RoutineNotFoundException::new);

        if (Boolean.FALSE.equals(rutina.getActiva())) {
            throw new RoutineNotFoundException();
        }

        DiaRutina diaRutina = diaRutinaRepository.obtenerPorIdYRutinaId(diaRutinaId, rutina.getId())
                .orElseThrow(DiaRutinaNotFoundException::new);

        ejercicioRutinaRepository.obtenerActivoPorIdYDiaRutinaId(ejercicioRutinaId, diaRutina.getId())
                .orElseThrow(EjercicioRutinaNotFoundException::new);

        return serieRepository.obtenerActivasPorEjercicioRutinaId(ejercicioRutinaId)
                .stream()
                .map(serie -> SerieEjercicioRutinaResponse.builder()
                        .id(serie.getId())
                        .ejercicioRutinaId(serie.getEjercicioRutinaId())
                        .orden(serie.getOrden())
                        .repeticionesMin(serie.getRepeticionesMin())
                        .repeticionesMax(serie.getRepeticionesMax())
                        .activo(serie.isActivo())
                        .build())
                .toList();
    }
}
