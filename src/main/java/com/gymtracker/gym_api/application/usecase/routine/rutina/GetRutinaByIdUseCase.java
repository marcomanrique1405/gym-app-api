package com.gymtracker.gym_api.application.usecase.routine.rutina;

import com.gymtracker.gym_api.application.dto.response.routine.RutinaResponse;
import com.gymtracker.gym_api.domain.model.routine.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetRutinaByIdUseCase {

    private final RutinaRepository rutinaRepository;
    private final SecurityUtils securityUtils;

    public GetRutinaByIdUseCase(RutinaRepository rutinaRepository, SecurityUtils securityUtils) {
        this.rutinaRepository = rutinaRepository;
        this.securityUtils = securityUtils;
    }

    public RutinaResponse obtenerRutina(UUID id) {

        UUID usuarioId = securityUtils.getCurrentUserId();

        Rutina rutina = rutinaRepository
                .obtenerPorIdYUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RoutineNotFoundException());

        return new RutinaResponse(
                rutina.getId(),
                rutina.getNombre(),
                rutina.getTipoProgresion()
        );
    }
}
