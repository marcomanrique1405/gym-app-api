package com.gymtracker.gym_api.application.usecase.workout;

import com.gymtracker.gym_api.application.dto.response.workout.SesionEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetSesionEntrenamientoActivaUseCase {

    private final SesionEntrenamientoRepository sesionEntrenamientoRepository;
    private final SecurityUtils securityUtils;

    public GetSesionEntrenamientoActivaUseCase(
            SesionEntrenamientoRepository sesionEntrenamientoRepository,
            SecurityUtils securityUtils
    ) {
        this.sesionEntrenamientoRepository = sesionEntrenamientoRepository;
        this.securityUtils = securityUtils;
    }

    public SesionEntrenamientoResponse obtenerSesionActiva() {
        UUID usuarioId = securityUtils.getCurrentUserId();

        SesionEntrenamiento sesionEntrenamiento = sesionEntrenamientoRepository
                .obtenerNoFinalizadaPorUsuarioId(usuarioId)
                .orElseThrow(SesionEntrenamientoNotFoundException::new);

        return new SesionEntrenamientoResponse(
                sesionEntrenamiento.getId(),
                sesionEntrenamiento.getUsuarioId(),
                sesionEntrenamiento.getRutinaId(),
                sesionEntrenamiento.getFechaInicio(),
                sesionEntrenamiento.getFechaFin(),
                sesionEntrenamiento.isFinalizada()
        );
    }

}
