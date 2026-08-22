package com.gymtracker.gym_api.application.usecase.exercise;

import com.gymtracker.gym_api.application.dto.request.exercise.EjercicioRequest;
import com.gymtracker.gym_api.application.dto.response.exercise.EjercicioResponse;
import com.gymtracker.gym_api.domain.model.exercise.Ejercicio;
import com.gymtracker.gym_api.domain.repository.exercise.EjercicioRepository;
import com.gymtracker.gym_api.shared.exception.exercise.EjercicioAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateEjercicioUseCase {

    private final EjercicioRepository ejercicioRepository;

    public CreateEjercicioUseCase(EjercicioRepository ejercicioRepository) {
        this.ejercicioRepository = ejercicioRepository;
    }

    public EjercicioResponse guardar(EjercicioRequest request) {

        String nombre = request.getNombre().trim();
        boolean existePorNombre = ejercicioRepository.existePorNombreYActiva(nombre);

        if (existePorNombre) {
            throw new EjercicioAlreadyExistsException();
        }

        Ejercicio ejercicio = Ejercicio.builder()
                .id(UUID.randomUUID())
                .nombre(nombre)
                .grupoMuscular(request.getGrupoMuscular())
                .descripcion(request.getDescripcion().trim())
                .activo(true)
                .build();

        Ejercicio ejercicioGuardado = ejercicioRepository.save(ejercicio);

        return new EjercicioResponse(
                ejercicioGuardado.getId(),
                ejercicioGuardado.getNombre(),
                ejercicioGuardado.getGrupoMuscular(),
                ejercicioGuardado.getDescripcion()
        );
    }
}
