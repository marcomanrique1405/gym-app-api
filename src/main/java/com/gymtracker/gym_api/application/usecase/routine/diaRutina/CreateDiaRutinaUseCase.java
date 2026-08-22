package com.gymtracker.gym_api.application.usecase.routine.diaRutina;

import com.gymtracker.gym_api.application.dto.request.routine.diaRutina.DiaRutinaRequest;
import com.gymtracker.gym_api.application.dto.response.routine.diaRutina.DiaRutinaResponse;
import com.gymtracker.gym_api.domain.model.routine.diaRutina.DiaRutina;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.diaRutina.DiaRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.diaRutina.DiaRutinaAlreadyExistsException;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateDiaRutinaUseCase {

    private final DiaRutinaRepository diaRutinaRepository;
    private final SecurityUtils securityUtils;
    private final RutinaRepository rutinaRepository;

    public CreateDiaRutinaUseCase(
            DiaRutinaRepository diaRutinaRepository,
            SecurityUtils securityUtils,
            RutinaRepository rutinaRepository
    ) {
        this.diaRutinaRepository = diaRutinaRepository;
        this.securityUtils = securityUtils;
        this.rutinaRepository = rutinaRepository;
    }

    public DiaRutinaResponse crear(UUID rutinaId, DiaRutinaRequest request) {

        UUID usuarioId = securityUtils.getCurrentUserId();

        Rutina rutina = rutinaRepository
                .obtenerPorIdYUsuarioId(rutinaId, usuarioId)
                .orElseThrow(RoutineNotFoundException::new);

        if (!rutina.getActiva()) {
            throw new RoutineNotFoundException();
        }

        if (diaRutinaRepository.existePorRutinaIdYDiaSemana(rutina.getId(), request.getDiaSemana())) {
            throw new DiaRutinaAlreadyExistsException();
        }

        int ordenDia = 1;
        while (diaRutinaRepository.existePorRutinaIdYOrdenDia(rutina.getId(), ordenDia)) {
            ordenDia++;
        }

        DiaRutina diaRutina = new DiaRutina(
                UUID.randomUUID(),
                rutina.getId(),
                request.getDiaSemana(),
                ordenDia
        );

        DiaRutina savedDiaRutina = diaRutinaRepository.save(diaRutina);

        return new DiaRutinaResponse(
                savedDiaRutina.getId(),
                savedDiaRutina.getRutinaId(),
                savedDiaRutina.getDiaSemana(),
                savedDiaRutina.getOrdenDia()
        );
    }

}
