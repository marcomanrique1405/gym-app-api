package com.gymtracker.gym_api.application.usecase.routine.rutina;

import com.gymtracker.gym_api.application.dto.request.routine.rutina.UpdateRutinaRequest;
import com.gymtracker.gym_api.application.dto.response.routine.rutina.RutinaResponse;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.routine.RoutineAlreadyExistsException;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateRutinaUseCase {

    private final RutinaRepository rutinaRepository;
    private final SecurityUtils securityUtils;

    public UpdateRutinaUseCase(RutinaRepository rutinaRepository, SecurityUtils securityUtils) {
        this.rutinaRepository = rutinaRepository;
        this.securityUtils = securityUtils;
    }

    public RutinaResponse actualizarRutina(UUID id, UpdateRutinaRequest request) {

        UUID usuarioId = securityUtils.getCurrentUserId();

        Rutina rutina = rutinaRepository
                .obtenerPorIdYUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RoutineNotFoundException());

        if (request.getNombre() != null) {

            String nombre = request.getNombre().trim();

            if (!nombre.equalsIgnoreCase(rutina.getNombre())) {

                if (rutinaRepository.existePorUsuarioIdYNombre(usuarioId, nombre)) {
                    throw new RoutineAlreadyExistsException();
                }

            }

            rutina.setNombre(nombre);
        }

        if (request.getTipoProgresion() != null) {
            rutina.setTipoProgresion(request.getTipoProgresion());
        }

        Rutina rutinaGuardada = rutinaRepository.save(rutina);

        return new RutinaResponse(
                rutinaGuardada.getId(),
                rutinaGuardada.getNombre(),
                rutinaGuardada.getTipoProgresion()
        );

    }

}
