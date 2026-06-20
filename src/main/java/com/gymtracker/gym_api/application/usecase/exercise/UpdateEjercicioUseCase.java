package com.gymtracker.gym_api.application.usecase.exercise;

import com.gymtracker.gym_api.application.dto.request.exercise.UpdateEjercicioRequest;
import com.gymtracker.gym_api.application.dto.response.exercise.EjercicioResponse;
import com.gymtracker.gym_api.domain.model.exercise.Ejercicio;
import com.gymtracker.gym_api.domain.repository.exercise.EjercicioRepository;
import com.gymtracker.gym_api.shared.exception.exercise.EjercicioNotFoundException;
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

        if (ejercicio.getNombre() != null) {
            ejercicio.setNombre(request.getNombre());
        }

        if (ejercicio.getGrupoMuscular() != null) {
            ejercicio.setGrupoMuscular(request.getGrupoMuscular());
        }

        if (ejercicio.getDescripcion() != null) {
            ejercicio.setDescripcion(request.getDescripcion());
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
