package com.gymtracker.gym_api.application.usecase.exercise;

import com.gymtracker.gym_api.application.dto.request.exercise.UpdateEjercicioRequest;
import com.gymtracker.gym_api.application.dto.response.exercise.EjercicioResponse;
import com.gymtracker.gym_api.domain.model.exercise.Ejercicio;
import com.gymtracker.gym_api.domain.repository.exercise.EjercicioRepository;
import com.gymtracker.gym_api.shared.exception.exercise.EjercicioNotFoundException;
import com.gymtracker.gym_api.shared.exception.exercise.EjercicioAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateEjercicioUseCase {

    private final EjercicioRepository ejercicioRepository;

    public UpdateEjercicioUseCase(EjercicioRepository ejercicioRepository) {
        this.ejercicioRepository = ejercicioRepository;
    }

    public EjercicioResponse actualiazarEjercicio(UUID id, UpdateEjercicioRequest request) {

        Ejercicio ejercicio = ejercicioRepository.obtenerPorId(id)
                .orElseThrow(() -> new EjercicioNotFoundException());

        if (!ejercicio.getActivo()) {
            throw new EjercicioNotFoundException();
        }

        if (request.getNombre() != null) {
            String nombre = request.getNombre().trim();
            if (nombre.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío");
            if (!nombre.equalsIgnoreCase(ejercicio.getNombre())
                    && ejercicioRepository.existePorNombreYActiva(nombre)) {
                throw new EjercicioAlreadyExistsException();
            }
            ejercicio.setNombre(nombre);
        }

        if (request.getGrupoMuscular() != null) {
            ejercicio.setGrupoMuscular(request.getGrupoMuscular());
        }

        if (request.getDescripcion() != null) {
            ejercicio.setDescripcion(request.getDescripcion().trim());
        }

        Ejercicio ejercicioSave = ejercicioRepository.save(ejercicio);

        return new EjercicioResponse(
                ejercicioSave.getId(),
                ejercicioSave.getNombre(),
                ejercicioSave.getGrupoMuscular(),
                ejercicioSave.getDescripcion()
        );
    }
}
