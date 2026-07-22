package com.gymtracker.gym_api.infrastructure.jpa.workout.serieEntrenamiento;

import com.gymtracker.gym_api.infrastructure.entity.workout.serieEntrenamiento.SerieEntrenamientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SerieEntrenamientoJpaRepository extends JpaRepository<SerieEntrenamientoEntity, UUID> {

    Optional<SerieEntrenamientoEntity> findByIdAndSesionEntrenamientoId(
            UUID id,
            UUID sesionEntrenamientoId
    );

    List<SerieEntrenamientoEntity> findBySesionEntrenamientoId(UUID sesionEntrenamientoId);

    boolean existsBySesionEntrenamientoIdAndSerieEjercicioRutinaId(
            UUID sesionEntrenamientoId,
            UUID serieEjercicioRutinaId
    );
}
