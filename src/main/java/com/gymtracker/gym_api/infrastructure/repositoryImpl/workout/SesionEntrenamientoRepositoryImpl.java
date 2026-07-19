package com.gymtracker.gym_api.infrastructure.repositoryImpl.workout;

import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.entity.workout.SesionEntrenamientoEntity;
import com.gymtracker.gym_api.infrastructure.jpa.workout.SesionEntrenamientoJpaRepository;
import com.gymtracker.gym_api.infrastructure.mapper.workout.SesionEntrenamientoMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SesionEntrenamientoRepositoryImpl implements SesionEntrenamientoRepository {

    private final SesionEntrenamientoJpaRepository sesionEntrenamientoJpaRepository;
    private final SesionEntrenamientoMapper sesionEntrenamientoMapper;

    public SesionEntrenamientoRepositoryImpl(
            SesionEntrenamientoJpaRepository sesionEntrenamientoJpaRepository,
            SesionEntrenamientoMapper sesionEntrenamientoMapper
    ) {
        this.sesionEntrenamientoJpaRepository = sesionEntrenamientoJpaRepository;
        this.sesionEntrenamientoMapper = sesionEntrenamientoMapper;
    }

    @Override
    public SesionEntrenamiento save(SesionEntrenamiento sesionEntrenamiento) {
        SesionEntrenamientoEntity entity = sesionEntrenamientoMapper.toEntity(sesionEntrenamiento);

        SesionEntrenamientoEntity guardado = sesionEntrenamientoJpaRepository.save(entity);

        return sesionEntrenamientoMapper.toDomain(guardado);
    }

    @Override
    public Optional<SesionEntrenamiento> obtenerPorIdYUsuarioId(
            UUID sesionEntrenamientoId,
            UUID usuarioId
    ) {
        return sesionEntrenamientoJpaRepository
                .findByIdAndUsuarioId(sesionEntrenamientoId, usuarioId)
                .map(sesionEntrenamientoMapper::toDomain);
    }

    @Override
    public Optional<SesionEntrenamiento> obtenerNoFinalizadaPorUsuarioId(UUID usuarioId) {
        return sesionEntrenamientoJpaRepository
                .findByUsuarioIdAndFinalizadaFalse(usuarioId)
                .map(sesionEntrenamientoMapper::toDomain);
    }

    @Override
    public List<SesionEntrenamiento> obtenerPorUsuarioId(UUID usuarioId) {
        return sesionEntrenamientoJpaRepository
                .findByUsuarioIdOrderByFechaInicioDesc(usuarioId)
                .stream()
                .map(sesionEntrenamientoMapper::toDomain)
                .toList();
    }

}
