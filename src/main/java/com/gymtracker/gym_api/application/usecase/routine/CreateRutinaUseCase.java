package com.gymtracker.gym_api.application.usecase.routine;

import com.gymtracker.gym_api.application.dto.request.routine.CreateRutinaRequest;
import com.gymtracker.gym_api.application.dto.response.routine.CreateRutinaResponse;
import com.gymtracker.gym_api.domain.model.routine.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.RutinaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CreateRutinaUseCase {

    private final RutinaRepository repository;


    public CreateRutinaUseCase(RutinaRepository repository) {
        this.repository = repository;
    }

    public CreateRutinaResponse crearRutina(CreateRutinaRequest request) {

        UUID usuarioId = obtenerUsuarioAutenticado();

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

        return new CreateRutinaResponse(
                guardada.getId(),
                guardada.getNombre(),
                guardada.getTipoProgresion()
        );

    }

    private UUID obtenerUsuarioAutenticado() {
        return UUID.fromString(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );
    }


}
