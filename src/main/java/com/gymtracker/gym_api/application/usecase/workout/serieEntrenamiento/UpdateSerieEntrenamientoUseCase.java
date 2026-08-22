package com.gymtracker.gym_api.application.usecase.workout.serieEntrenamiento;

import com.gymtracker.gym_api.application.dto.request.workout.serieEntrenamiento.UpdateSerieEntrenamientoRequest;
import com.gymtracker.gym_api.application.dto.response.workout.serieEntrenamiento.SerieEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.model.workout.serieEntrenamiento.SerieEntrenamiento;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.domain.repository.workout.serieEntrenamiento.SerieEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoAlreadyFinishedException;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoNotFoundException;
import com.gymtracker.gym_api.shared.exception.workout.serieEntrenamiento.SerieEntrenamientoNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UpdateSerieEntrenamientoUseCase {

    private final SerieEntrenamientoRepository serieEntrenamientoRepository;
    private final SesionEntrenamientoRepository sesionEntrenamientoRepository;
    private final SecurityUtils securityUtils;

    public UpdateSerieEntrenamientoUseCase(
            SerieEntrenamientoRepository serieEntrenamientoRepository,
            SesionEntrenamientoRepository sesionEntrenamientoRepository,
            SecurityUtils securityUtils
    ) {
        this.serieEntrenamientoRepository = serieEntrenamientoRepository;
        this.sesionEntrenamientoRepository = sesionEntrenamientoRepository;
        this.securityUtils = securityUtils;
    }

    public SerieEntrenamientoResponse actualizar(
            UUID sesionEntrenamientoId,
            UUID serieEntrenamientoId,
            UpdateSerieEntrenamientoRequest request
    ) {
        UUID usuarioId = securityUtils.getCurrentUserId();

        SesionEntrenamiento sesion = sesionEntrenamientoRepository
                .obtenerPorIdYUsuarioId(sesionEntrenamientoId, usuarioId)
                .orElseThrow(SesionEntrenamientoNotFoundException::new);

        if (sesion.isFinalizada()) {
            throw new SesionEntrenamientoAlreadyFinishedException();
        }

        SerieEntrenamiento actual = serieEntrenamientoRepository
                .obtenerPorIdYSesionEntrenamientoId(serieEntrenamientoId, sesion.getId())
                .orElseThrow(SerieEntrenamientoNotFoundException::new);

        Integer repeticiones = request.getRepeticionesRealizadas() != null
                ? request.getRepeticionesRealizadas()
                : actual.getRepeticionesRealizadas();

        BigDecimal peso = request.getPesoUtilizado() != null
                ? request.getPesoUtilizado()
                : actual.getPesoUtilizado();

        SerieEntrenamiento actualizada = SerieEntrenamiento.builder()
                .id(actual.getId())
                .sesionEntrenamientoId(actual.getSesionEntrenamientoId())
                .serieEjercicioRutinaId(actual.getSerieEjercicioRutinaId())
                .repeticionesRealizadas(repeticiones)
                .pesoUtilizado(peso)
                .build();

        SerieEntrenamiento guardada = serieEntrenamientoRepository.save(actualizada);

        return SerieEntrenamientoResponse.builder()
                .id(guardada.getId())
                .sesionEntrenamientoId(guardada.getSesionEntrenamientoId())
                .serieEjercicioRutinaId(guardada.getSerieEjercicioRutinaId())
                .repeticionesRealizadas(guardada.getRepeticionesRealizadas())
                .pesoUtilizado(guardada.getPesoUtilizado())
                .build();
    }
}
