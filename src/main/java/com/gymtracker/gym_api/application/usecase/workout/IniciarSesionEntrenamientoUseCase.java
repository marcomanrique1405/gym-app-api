package com.gymtracker.gym_api.application.usecase.workout;

import com.gymtracker.gym_api.application.dto.response.workout.SesionEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoAlreadyActiveException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class IniciarSesionEntrenamientoUseCase {

    private final SesionEntrenamientoRepository sesionEntrenamientoRepository;
    private final RutinaRepository rutinaRepository;
    private final SecurityUtils securityUtils;

    public IniciarSesionEntrenamientoUseCase(
            SesionEntrenamientoRepository sesionEntrenamientoRepository,
            RutinaRepository rutinaRepository,
            SecurityUtils securityUtils
    ) {
        this.sesionEntrenamientoRepository = sesionEntrenamientoRepository;
        this.rutinaRepository = rutinaRepository;
        this.securityUtils = securityUtils;
    }

    public SesionEntrenamientoResponse iniciarSesion(UUID rutinaId) {
        UUID usuarioId = securityUtils.getCurrentUserId();

        Rutina rutina = rutinaRepository
                .obtenerPorIdYUsuarioId(rutinaId, usuarioId)
                .orElseThrow(RoutineNotFoundException::new);

        if (!rutina.getActiva()) {
            throw new RoutineNotFoundException();
        }

        if (sesionEntrenamientoRepository.obtenerNoFinalizadaPorUsuarioId(usuarioId).isPresent()) {
            throw new SesionEntrenamientoAlreadyActiveException();
        }

        SesionEntrenamiento sesionEntrenamiento = new SesionEntrenamiento(
                UUID.randomUUID(),
                usuarioId,
                rutina.getId(),
                LocalDateTime.now(),
                null,
                false
        );

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
