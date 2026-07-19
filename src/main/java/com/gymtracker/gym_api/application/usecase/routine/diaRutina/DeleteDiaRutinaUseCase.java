package com.gymtracker.gym_api.application.usecase.routine.diaRutina;

import com.gymtracker.gym_api.domain.model.routine.diaRutina.DiaRutina;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.diaRutina.DiaRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.diaRutina.DiaRutinaNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteDiaRutinaUseCase {

    private final DiaRutinaRepository diaRutinaRepository;
    private final SecurityUtils securityUtils;
    private final RutinaRepository rutinaRepository;

    public DeleteDiaRutinaUseCase(DiaRutinaRepository diaRutinaRepository, SecurityUtils securityUtils, RutinaRepository rutinaRepository) {
        this.diaRutinaRepository = diaRutinaRepository;
        this.securityUtils = securityUtils;
        this.rutinaRepository = rutinaRepository;
    }

    public void delet(UUID rutinaId, UUID diaRutinaId) {

        UUID usuarioId = securityUtils.getCurrentUserId();

        Rutina rutina = rutinaRepository
                .obtenerPorIdYUsuarioId(rutinaId, usuarioId)
                .orElseThrow(RoutineNotFoundException::new);

        if (!rutina.getActiva()) {
            throw new RoutineNotFoundException();
        }

        DiaRutina diaRutina = diaRutinaRepository
                .obtenerPorIdYRutinaId(diaRutinaId, rutinaId)
                .orElseThrow(DiaRutinaNotFoundException::new);

        diaRutinaRepository.delete(diaRutina);

    }
}
