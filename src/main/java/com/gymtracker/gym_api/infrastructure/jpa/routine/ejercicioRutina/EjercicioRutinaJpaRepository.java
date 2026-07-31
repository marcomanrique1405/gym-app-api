package com.gymtracker.gym_api.infrastructure.jpa.routine.ejercicioRutina;

import com.gymtracker.gym_api.infrastructure.entity.routine.ejercicioRutina.EjercicioRutinaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EjercicioRutinaJpaRepository extends JpaRepository<EjercicioRutinaEntity, UUID> {

    Optional<EjercicioRutinaEntity> findByIdAndActivoTrue(UUID id);

    List<EjercicioRutinaEntity> findByDiaRutinaIdAndActivoTrueOrderByOrdenAsc(UUID diaRutinaId);

    Optional<EjercicioRutinaEntity> findByIdAndDiaRutinaIdAndActivoTrue(
            UUID id,
            UUID diaRutinaId
    );

    boolean existsByDiaRutinaIdAndEjercicioIdAndActivoTrue(
            UUID diaRutinaId,
            UUID ejercicioId
    );

    boolean existsByDiaRutinaIdAndEjercicioIdAndActivoTrueAndIdNot(
            UUID diaRutinaId, UUID ejercicioId, UUID id);

    boolean existsByDiaRutinaIdAndOrdenAndActivoTrue(
            UUID diaRutinaId,
            int orden
    );

    int countByDiaRutinaIdAndActivoTrue(UUID diaRutinaId);
}
