package com.gymtracker.gym_api.application.usecase.workout;

import com.gymtracker.gym_api.application.dto.response.workout.SesionEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoAlreadyFinishedException;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FinalizarSesionEntrenamientoUseCase {

    private final SesionEntrenamientoRepository sesionEntrenamientoRepository;
    private final SecurityUtils securityUtils;

    public FinalizarSesionEntrenamientoUseCase(
            SesionEntrenamientoRepository sesionEntrenamientoRepository,
            SecurityUtils securityUtils
    ) {
        this.sesionEntrenamientoRepository = sesionEntrenamientoRepository;
        this.securityUtils = securityUtils;
    }

    public SesionEntrenamientoResponse finalizarSesion(UUID sesionEntrenamientoId) {
        UUID usuarioId = securityUtils.getCurrentUserId();

        SesionEntrenamiento sesionEntrenamiento = sesionEntrenamientoRepository
                .obtenerPorIdYUsuarioId(sesionEntrenamientoId, usuarioId)
                .orElseThrow(SesionEntrenamientoNotFoundException::new);

        if (sesionEntrenamiento.isFinalizada()) {
            throw new SesionEntrenamientoAlreadyFinishedException();
        }

        sesionEntrenamiento.setFechaFin(LocalDateTime.now());
        sesionEntrenamiento.setFinalizada(true);

        SesionEntrenamiento guardada = sesionEntrenamientoRepository.save(sesionEntrenamiento);

        return new SesionEntrenamientoResponse(
                guardada.getId(),
                guardada.getUsuarioId(),
                guardada.getRutinaId(),
                guardada.getFechaInicio(),
                guardada.getFechaFin(),
                guardada.isFinalizada()
        );
    }

}
