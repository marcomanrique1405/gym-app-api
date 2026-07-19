package com.gymtracker.gym_api.application.usecase.routine.rutina;

import com.gymtracker.gym_api.application.dto.request.routine.rutina.CreateRutinaRequest;
import com.gymtracker.gym_api.application.dto.response.routine.rutina.RutinaResponse;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CreateRutinaUseCase {

    private final RutinaRepository repository;
    private final SecurityUtils securityUtils;


    public CreateRutinaUseCase(RutinaRepository repository, SecurityUtils securityUtils) {
        this.repository = repository;
        this.securityUtils = securityUtils;
    }

    public RutinaResponse crearRutina(CreateRutinaRequest request) {

        UUID usuarioId = securityUtils.getCurrentUserId();

        String nombre = request.getNombre().trim();

        Rutina rutina = new Rutina(
                UUID.randomUUID(),
                usuarioId,
                nombre,
                request.getTipoProgresion(),
                true,
                LocalDateTime.now()
        );

        Rutina guardada = repository.save(rutina);

        return new RutinaResponse(
                guardada.getId(),
                guardada.getNombre(),
                guardada.getTipoProgresion()
        );

    }

}
