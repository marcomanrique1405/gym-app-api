package com.gymtracker.gym_api.infrastructure.jpa.workout;

import com.gymtracker.gym_api.infrastructure.entity.workout.SesionEntrenamientoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SesionEntrenamientoJpaRepository extends JpaRepository<SesionEntrenamientoEntity, UUID> {

    Optional<SesionEntrenamientoEntity> findByIdAndUsuarioId(UUID id, UUID usuarioId);

    Optional<SesionEntrenamientoEntity> findByUsuarioIdAndFinalizadaFalse(UUID usuarioId);

    Page<SesionEntrenamientoEntity> findByUsuarioId(UUID usuarioId, Pageable pageable);

}
