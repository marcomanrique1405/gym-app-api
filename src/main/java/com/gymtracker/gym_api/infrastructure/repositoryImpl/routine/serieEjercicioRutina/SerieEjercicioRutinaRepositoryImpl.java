package com.gymtracker.gym_api.infrastructure.repositoryImpl.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.domain.model.routine.serieEjercicioRutina.SerieEjercicioRutina;
import com.gymtracker.gym_api.domain.repository.routine.serieEjercicioRutina.SerieEjercicioRutinaRepository;
import com.gymtracker.gym_api.infrastructure.entity.routine.serieEjercicioRutina.SerieEjercicioRutinaEntity;
import com.gymtracker.gym_api.infrastructure.jpa.routine.serieEjercicioRutina.SerieEjercicioRutinaJpaRepository;
import com.gymtracker.gym_api.infrastructure.mapper.routine.serieEjercicioRutina.SerieEjercicioRutinaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SerieEjercicioRutinaRepositoryImpl implements SerieEjercicioRutinaRepository {

    private final SerieEjercicioRutinaJpaRepository serieEjercicioRutinaJpaRepository;
    private final SerieEjercicioRutinaMapper serieEjercicioRutinaMapper;

    public SerieEjercicioRutinaRepositoryImpl(
            SerieEjercicioRutinaJpaRepository serieEjercicioRutinaJpaRepository,
            SerieEjercicioRutinaMapper serieEjercicioRutinaMapper
    ) {
        this.serieEjercicioRutinaJpaRepository = serieEjercicioRutinaJpaRepository;
        this.serieEjercicioRutinaMapper = serieEjercicioRutinaMapper;
    }

    @Override
    public SerieEjercicioRutina save(SerieEjercicioRutina serieEjercicioRutina) {

        SerieEjercicioRutinaEntity entity = serieEjercicioRutinaMapper.toEntity(serieEjercicioRutina);

        SerieEjercicioRutinaEntity savedEntity = serieEjercicioRutinaJpaRepository.save(entity);

        return serieEjercicioRutinaMapper.toDomain(savedEntity);
    }

    @Override
    public List<SerieEjercicioRutina> obtenerActivasPorEjercicioRutinaId(UUID ejercicioRutinaId) {
        return serieEjercicioRutinaJpaRepository
                .findByEjercicioRutinaIdAndActivoTrueOrderByOrdenAsc(ejercicioRutinaId)
                .stream()
                .map(serieEjercicioRutinaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<SerieEjercicioRutina> obtenerActivaPorIdYEjercicioRutinaId(
            UUID serieEjercicioRutinaId,
            UUID ejercicioRutinaId
    ) {
        return serieEjercicioRutinaJpaRepository
                .findByIdAndEjercicioRutinaIdAndActivoTrue(serieEjercicioRutinaId, ejercicioRutinaId)
                .map(serieEjercicioRutinaMapper::toDomain);
    }

    @Override
    public boolean existeActivaPorEjercicioRutinaIdYOrden(UUID ejercicioRutinaId, int orden) {
        return serieEjercicioRutinaJpaRepository
                .existsByEjercicioRutinaIdAndOrdenAndActivoTrue(ejercicioRutinaId, orden);
    }

    @Override
    public int contarActivasPorEjercicioRutinaId(UUID ejercicioRutinaId) {
        return serieEjercicioRutinaJpaRepository.countByEjercicioRutinaIdAndActivoTrue(ejercicioRutinaId);
    }
}
