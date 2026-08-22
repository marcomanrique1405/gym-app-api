package com.gymtracker.gym_api.application.usecase.workout;

import com.gymtracker.gym_api.application.dto.response.PageResponse;
import com.gymtracker.gym_api.application.dto.response.workout.SesionEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.PageResult;
import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import org.springframework.stereotype.Service;

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

    public PageResponse<SesionEntrenamientoResponse> obtenerHistorial(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("El número de página no puede ser negativo");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("El tamaño de página debe estar entre 1 y 100");
        }

        UUID usuarioId = securityUtils.getCurrentUserId();
        PageResult<SesionEntrenamiento> result =
                sesionEntrenamientoRepository.obtenerPorUsuarioId(usuarioId, page, size);

        var content = result.content().stream()
                .map(s -> new SesionEntrenamientoResponse(
                        s.getId(),
                        s.getUsuarioId(),
                        s.getRutinaId(),
                        s.getFechaInicio(),
                        s.getFechaFin(),
                        s.isFinalizada()
                ))
                .toList();

        return new PageResponse<>(
                content,
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages(),
                result.page() == 0,
                result.totalPages() == 0 || result.page() >= result.totalPages() - 1
        );
    }

}
