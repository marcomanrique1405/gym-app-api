package com.gymtracker.gym_api.infrastructure.repositoryImpl.exercise;

import com.gymtracker.gym_api.domain.enums.GrupoMuscular;
import com.gymtracker.gym_api.domain.model.exercise.Ejercicio;
import com.gymtracker.gym_api.domain.repository.exercise.EjercicioRepository;
import com.gymtracker.gym_api.infrastructure.entity.exercise.EjercicioEntity;
import com.gymtracker.gym_api.infrastructure.jpa.exercise.EjercicioJpaRepository;
import com.gymtracker.gym_api.infrastructure.mapper.exercise.EjercicioMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EjercicioRepositoryImpl implements EjercicioRepository {

    private final EjercicioMapper ejercicioMapper;
    private final EjercicioJpaRepository ejercicioJpaRepository;

    public EjercicioRepositoryImpl(
            EjercicioMapper ejercicioMapper,
            EjercicioJpaRepository ejercicioJpaRepository
    ) {
        this.ejercicioMapper = ejercicioMapper;
        this.ejercicioJpaRepository = ejercicioJpaRepository;
    }

    @Override
    public Ejercicio save(Ejercicio ejercicio) {

        EjercicioEntity entity = ejercicioMapper.toEntity(ejercicio);

        EjercicioEntity ejercicioGuardado = ejercicioJpaRepository.save(entity);

        return ejercicioMapper.toDomain(ejercicioGuardado);
    }

    @Override
    public List<Ejercicio> obtenerPorGrupoMuscular(GrupoMuscular grupoMuscular) {
        return ejercicioJpaRepository
                .findByGrupoMuscularAndActivoTrueOrderByNombreAsc(grupoMuscular)
                .stream()
                .map(ejercicioMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorNombreYActiva(String nombre) {
        return ejercicioJpaRepository.existsByNombreAndActivoTrue(nombre);
    }

    @Override
    public Optional<Ejercicio> obtenerPorId(UUID id) {
        return ejercicioJpaRepository.findById(id)
                .map(ejercicioMapper::toDomain);
    }
}
