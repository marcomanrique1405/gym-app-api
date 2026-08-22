package com.gymtracker.gym_api.domain.repository.exercise;

import com.gymtracker.gym_api.domain.enums.GrupoMuscular;
import com.gymtracker.gym_api.domain.model.exercise.Ejercicio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface EjercicioRepository {

    Ejercicio save(Ejercicio ejercicio);

    List<Ejercicio> obtenerPorGrupoMuscular(GrupoMuscular grupoMuscular);

    boolean existePorNombreYActiva(String nombre);

    Optional<Ejercicio> obtenerPorId(UUID id);

}
