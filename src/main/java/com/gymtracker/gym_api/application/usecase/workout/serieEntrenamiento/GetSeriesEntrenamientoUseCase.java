package com.gymtracker.gym_api.application.usecase.workout.serieEntrenamiento;

import com.gymtracker.gym_api.application.dto.response.workout.serieEntrenamiento.SerieEntrenamientoResponse;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.domain.repository.workout.serieEntrenamiento.SerieEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetSeriesEntrenamientoUseCase {

    private final SerieEntrenamientoRepository serieEntrenamientoRepository;
    private final SesionEntrenamientoRepository sesionEntrenamientoRepository;
    private final SecurityUtils securityUtils;

    public GetSeriesEntrenamientoUseCase(
            SerieEntrenamientoRepository serieEntrenamientoRepository,
            SesionEntrenamientoRepository sesionEntrenamientoRepository,
            SecurityUtils securityUtils
    ) {
        this.serieEntrenamientoRepository = serieEntrenamientoRepository;
        this.sesionEntrenamientoRepository = sesionEntrenamientoRepository;
        this.securityUtils = securityUtils;
    }

    public List<SerieEntrenamientoResponse> obtenerPorSesion(UUID sesionEntrenamientoId) {
        UUID usuarioId = securityUtils.getCurrentUserId();

        sesionEntrenamientoRepository
                .obtenerPorIdYUsuarioId(sesionEntrenamientoId, usuarioId)
                .orElseThrow(SesionEntrenamientoNotFoundException::new);

        return serieEntrenamientoRepository.obtenerPorSesionEntrenamientoId(sesionEntrenamientoId)
                .stream()
                .map(serie -> SerieEntrenamientoResponse.builder()
                        .id(serie.getId())
                        .sesionEntrenamientoId(serie.getSesionEntrenamientoId())
                        .serieEjercicioRutinaId(serie.getSerieEjercicioRutinaId())
                        .repeticionesRealizadas(serie.getRepeticionesRealizadas())
                        .pesoUtilizado(serie.getPesoUtilizado())
                        .build())
                .toList();
    }
}
