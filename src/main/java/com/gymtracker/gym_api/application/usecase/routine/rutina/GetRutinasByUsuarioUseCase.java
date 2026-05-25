package com.gymtracker.gym_api.application.usecase.routine.rutina;

import com.gymtracker.gym_api.application.dto.response.routine.rutina.RutinaResponse;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetRutinasByUsuarioUseCase {

    private final RutinaRepository rutinaRepository;
    private final SecurityUtils securityUtils;

    public GetRutinasByUsuarioUseCase(RutinaRepository rutinaRepository, SecurityUtils securityUtils) {
        this.rutinaRepository = rutinaRepository;
        this.securityUtils = securityUtils;
    }

    public List<RutinaResponse> obtenerRutinas() {

        UUID usuarioId = securityUtils.getCurrentUserId();

        List<Rutina> rutinas = rutinaRepository.obtenerPorUsuarioIdYActivaTrue(usuarioId);

        return rutinas.stream()
                .map(r -> new RutinaResponse(
                        r.getId(),
                        r.getNombre(),
                        r.getTipoProgresion()
                ))
                .toList();

    }

}