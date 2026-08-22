package com.gymtracker.gym_api.infrastructure.repositoryImpl.workout.serieEntrenamiento;

import com.gymtracker.gym_api.domain.model.workout.serieEntrenamiento.SerieEntrenamiento;
import com.gymtracker.gym_api.domain.repository.workout.serieEntrenamiento.SerieEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.jpa.workout.serieEntrenamiento.SerieEntrenamientoJpaRepository;
import com.gymtracker.gym_api.infrastructure.mapper.workout.serieEntrenamiento.SerieEntrenamientoMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SerieEntrenamientoRepositoryImpl implements SerieEntrenamientoRepository {

    private final SerieEntrenamientoJpaRepository serieEntrenamientoJpaRepository;
    private final SerieEntrenamientoMapper serieEntrenamientoMapper;

    public SerieEntrenamientoRepositoryImpl(
            SerieEntrenamientoJpaRepository serieEntrenamientoJpaRepository,
            SerieEntrenamientoMapper serieEntrenamientoMapper
    ) {
        this.serieEntrenamientoJpaRepository = serieEntrenamientoJpaRepository;
        this.serieEntrenamientoMapper = serieEntrenamientoMapper;
    }

    @Override
    public SerieEntrenamiento save(SerieEntrenamiento serieEntrenamiento) {
        return serieEntrenamientoMapper.toDomain(
                serieEntrenamientoJpaRepository.save(
                        serieEntrenamientoMapper.toEntity(serieEntrenamiento)
                )
        );
    }

    @Override
    public Optional<SerieEntrenamiento> obtenerPorIdYSesionEntrenamientoId(
            UUID serieEntrenamientoId,
            UUID sesionEntrenamientoId
    ) {
        return serieEntrenamientoJpaRepository
                .findByIdAndSesionEntrenamientoId(serieEntrenamientoId, sesionEntrenamientoId)
                .map(serieEntrenamientoMapper::toDomain);
    }

    @Override
    public List<SerieEntrenamiento> obtenerPorSesionEntrenamientoId(UUID sesionEntrenamientoId) {
        return serieEntrenamientoJpaRepository.findBySesionEntrenamientoId(sesionEntrenamientoId)
                .stream()
                .map(serieEntrenamientoMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorSesionEntrenamientoIdYSerieEjercicioRutinaId(
            UUID sesionEntrenamientoId,
            UUID serieEjercicioRutinaId
    ) {
        return serieEntrenamientoJpaRepository
                .existsBySesionEntrenamientoIdAndSerieEjercicioRutinaId(
                        sesionEntrenamientoId,
                        serieEjercicioRutinaId
                );
    }
}
