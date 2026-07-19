package com.gymtracker.gym_api.application.usecase.routine.diaRutina;

import com.gymtracker.gym_api.application.dto.response.routine.diaRutina.DiaRutinaResponse;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.diaRutina.DiaRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetDiasRutinaUseCase {

    private final DiaRutinaRepository diaRutinaRepository;
    private final SecurityUtils securityUtils;
    private final RutinaRepository rutinaRepository;

    public GetDiasRutinaUseCase(DiaRutinaRepository diaRutinaRepository, SecurityUtils securityUtils, RutinaRepository rutinaRepository) {
        this.diaRutinaRepository = diaRutinaRepository;
        this.securityUtils = securityUtils;
        this.rutinaRepository = rutinaRepository;
    }

    public List<DiaRutinaResponse> obtenerDiasPorRutina(UUID rutinaId) {

        UUID usuarioId = securityUtils.getCurrentUserId();

        Rutina rutina = rutinaRepository
                .obtenerPorIdYUsuarioId(rutinaId, usuarioId)
                .orElseThrow(RoutineNotFoundException::new);

        if (!rutina.getActiva()) {
            throw new RoutineNotFoundException();
        }

        return diaRutinaRepository
                .obtenerPorRutinaId(rutina.getId())
                .stream()
                .map( r -> new DiaRutinaResponse(
                        r.getId(),
                        r.getRutinaId(),
                        r.getDiaSemana(),
                        r.getOrdenDia()

                ))
                .toList();

    }

}
