package com.gymtracker.gym_api.application.usecase.routine.rutina;

import com.gymtracker.gym_api.domain.model.routine.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteRutinaUseCase {

    private final RutinaRepository rutinaRepository;
    private final SecurityUtils securityUtils;

    public DeleteRutinaUseCase(RutinaRepository rutinaRepository, SecurityUtils securityUtils) {
        this.rutinaRepository = rutinaRepository;
        this.securityUtils = securityUtils;
    }

    public void eliminarRutina(UUID id) {

        UUID usuarioId = securityUtils.getCurrentUserId();

        Rutina rutina = rutinaRepository
                .obtenerPorIdYUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RoutineNotFoundException());

        rutina.setActiva(false);

        rutinaRepository.save(rutina);
    }
}
