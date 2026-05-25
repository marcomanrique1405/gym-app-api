package com.gymtracker.gym_api.application.usecase.routine.diaRutina;

import com.gymtracker.gym_api.application.dto.request.routine.diaRutina.UpdateDiaRutinaRequest;
import com.gymtracker.gym_api.application.dto.response.routine.diaRutina.DiaRutinaResponse;
import com.gymtracker.gym_api.domain.model.routine.diaRutina.DiaRutina;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.diaRutina.DiaRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.diaRutina.DiaRutinaAlreadyExistsException;
import com.gymtracker.gym_api.shared.exception.diaRutina.DiaRutinaNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateDiaRutinaUseCase {

    private final DiaRutinaRepository diaRutinaRepository;
    private final SecurityUtils securityUtils;
    private final RutinaRepository rutinaRepository;

    public UpdateDiaRutinaUseCase(
            DiaRutinaRepository diaRutinaRepository,
            SecurityUtils securityUtils,
            RutinaRepository rutinaRepository
    ) {
        this.diaRutinaRepository = diaRutinaRepository;
        this.securityUtils = securityUtils;
        this.rutinaRepository = rutinaRepository;
    }

    public DiaRutinaResponse update(
            UUID rutinaId,
            UUID diaRutinaId,
            UpdateDiaRutinaRequest request
    ) {

        UUID usuarioId = securityUtils.getCurrentUserId();

        Rutina rutina = rutinaRepository
                .obtenerPorIdYUsuarioId(rutinaId, usuarioId)
                .orElseThrow(RoutineNotFoundException::new);

        if (!rutina.getActiva()) {
            throw new RoutineNotFoundException();
        }

        DiaRutina diaRutina = diaRutinaRepository
                .obtenerPorIdYRutinaId(diaRutinaId, rutina.getId())
                .orElseThrow(DiaRutinaNotFoundException::new);

        boolean cambioDiaSemana = !diaRutina.getDiaSemana().equals(request.getDiaSemana());

        if (cambioDiaSemana &&
                diaRutinaRepository.existePorRutinaIdYDiaSemana(rutina.getId(), request.getDiaSemana())) {
            throw new DiaRutinaAlreadyExistsException();
        }

        diaRutina.actualizarDiaSemana(request.getDiaSemana());

        DiaRutina diaRutinaSave = diaRutinaRepository.save(diaRutina);

        return DiaRutinaResponse.builder()
                .id(diaRutinaSave.getId())
                .rutinaId(diaRutinaSave.getRutinaId())
                .diaSemana(diaRutinaSave.getDiaSemana())
                .ordenDia(diaRutinaSave.getOrdenDia())
                .build();
    }
}
