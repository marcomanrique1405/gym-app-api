package com.gymtracker.gym_api.application.usecase.exercise;

import com.gymtracker.gym_api.application.dto.response.exercise.EjercicioResponse;
import com.gymtracker.gym_api.domain.enums.GrupoMuscular;
import com.gymtracker.gym_api.domain.model.exercise.Ejercicio;
import com.gymtracker.gym_api.domain.repository.exercise.EjercicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetEjerciciosPorGrupoMuscularUseCase {

    private final EjercicioRepository ejercicioRepository;

    public GetEjerciciosPorGrupoMuscularUseCase(EjercicioRepository ejercicioRepository) {
        this.ejercicioRepository = ejercicioRepository;
    }

    public List<EjercicioResponse> obtnerEjerciciosPorGrupoMuscular(GrupoMuscular grupoMuscular) {

        List<Ejercicio> ejercicios = ejercicioRepository.obtenerPorGrupoMuscular(grupoMuscular);

        return ejercicios.stream()
                .map(r -> new EjercicioResponse(
                        r.getId(),
                        r.getNombre(),
                        r.getGrupoMuscular(),
                        r.getDescripcion()

                ))
                .toList();
    }
}
