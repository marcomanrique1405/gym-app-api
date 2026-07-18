package com.gymtracker.gym_api.infrastructure.jpa.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.infrastructure.entity.routine.serieEjercicioRutina.SerieEjercicioRutinaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SerieEjercicioRutinaJpaRepository extends JpaRepository<SerieEjercicioRutinaEntity, UUID> {

    List<SerieEjercicioRutinaEntity> findByEjercicioRutinaIdAndActivoTrueOrderByOrdenAsc(UUID ejercicioRutinaId);

    Optional<SerieEjercicioRutinaEntity> findByIdAndEjercicioRutinaIdAndActivoTrue(
            UUID id,
            UUID ejercicioRutinaId
    );

    boolean existsByEjercicioRutinaIdAndOrdenAndActivoTrue(
            UUID ejercicioRutinaId,
            int orden
    );

    int countByEjercicioRutinaIdAndActivoTrue(UUID ejercicioRutinaId);
}
