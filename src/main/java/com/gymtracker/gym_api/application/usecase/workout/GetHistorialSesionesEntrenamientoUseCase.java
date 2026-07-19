package com.gymtracker.gym_api.application.usecase.workout;

import com.gymtracker.gym_api.application.dto.response.workout.SesionEntrenamientoResponse;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetHistorialSesionesEntrenamientoUseCase {

    private final SesionEntrenamientoRepository sesionEntrenamientoRepository;
    private final SecurityUtils securityUtils;

    public GetHistorialSesionesEntrenamientoUseCase(
            SesionEntrenamientoRepository sesionEntrenamientoRepository,
            SecurityUtils securityUtils
    ) {
        this.sesionEntrenamientoRepository = sesionEntrenamientoRepository;
        this.securityUtils = securityUtils;
    }

    public List<SesionEntrenamientoResponse> obtenerHistorial() {
        UUID usuarioId = securityUtils.getCurrentUserId();

        return sesionEntrenamientoRepository
                .obtenerPorUsuarioId(usuarioId)
                .stream()
                .map(s -> new SesionEntrenamientoResponse(
                        s.getId(),
                        s.getUsuarioId(),
                        s.getRutinaId(),
                        s.getFechaInicio(),
                        s.getFechaFin(),
                        s.isFinalizada()
                ))
                .toList();
    }

}
