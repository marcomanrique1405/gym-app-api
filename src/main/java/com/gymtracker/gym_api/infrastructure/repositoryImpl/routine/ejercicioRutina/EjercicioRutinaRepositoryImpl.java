package com.gymtracker.gym_api.infrastructure.repositoryImpl.routine.ejercicioRutina;

import com.gymtracker.gym_api.domain.model.routine.ejercicioRutina.EjercicioRutina;
import com.gymtracker.gym_api.domain.repository.routine.ejercicioRutina.EjercicioRutinaRepository;
import com.gymtracker.gym_api.infrastructure.entity.routine.ejercicioRutina.EjercicioRutinaEntity;
import com.gymtracker.gym_api.infrastructure.jpa.routine.ejercicioRutina.EjercicioRutinaJpaRepository;
import com.gymtracker.gym_api.infrastructure.mapper.routine.ejercicioRutina.EjercicioRutinaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EjercicioRutinaRepositoryImpl implements EjercicioRutinaRepository {

    private final EjercicioRutinaJpaRepository ejercicioRutinaJpaRepository;
    private final EjercicioRutinaMapper ejercicioRutinaMapper;

    public EjercicioRutinaRepositoryImpl(
            EjercicioRutinaJpaRepository ejercicioRutinaJpaRepository,
            EjercicioRutinaMapper ejercicioRutinaMapper
    ) {
        this.ejercicioRutinaJpaRepository = ejercicioRutinaJpaRepository;
        this.ejercicioRutinaMapper = ejercicioRutinaMapper;
    }

    @Override
    public EjercicioRutina save(EjercicioRutina ejercicioRutina) {

        EjercicioRutinaEntity entity = ejercicioRutinaMapper.toEntity(ejercicioRutina);

        EjercicioRutinaEntity savedEntity = ejercicioRutinaJpaRepository.save(entity);

        return ejercicioRutinaMapper.toDomain(savedEntity);
    }

    @Override
    public List<EjercicioRutina> obtenerActivosPorDiaRutinaId(UUID diaRutinaId) {
        return ejercicioRutinaJpaRepository
                .findByDiaRutinaIdAndActivoTrueOrderByOrdenAsc(diaRutinaId)
                .stream()
                .map(ejercicioRutinaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<EjercicioRutina> obtenerActivoPorIdYDiaRutinaId(UUID ejercicioRutinaId, UUID diaRutinaId) {
        return ejercicioRutinaJpaRepository
                .findByIdAndDiaRutinaIdAndActivoTrue(ejercicioRutinaId, diaRutinaId)
                .map(ejercicioRutinaMapper::toDomain);
    }

    @Override
    public boolean existeActivoPorDiaRutinaIdYEjercicioId(UUID diaRutinaId, UUID ejercicioId) {
        return ejercicioRutinaJpaRepository
                .existsByDiaRutinaIdAndEjercicioIdAndActivoTrue(diaRutinaId, ejercicioId);
    }

    @Override
    public boolean existeActivoPorDiaRutinaIdYOrden(UUID diaRutinaId, int orden) {
        return ejercicioRutinaJpaRepository
                .existsByDiaRutinaIdAndOrdenAndActivoTrue(diaRutinaId, orden);
    }

    @Override
    public int contarActivosPorDiaRutinaId(UUID diaRutinaId) {
        return ejercicioRutinaJpaRepository.countByDiaRutinaIdAndActivoTrue(diaRutinaId);
    }
}