package com.gymtracker.gym_api.infrastructure.jpa.routine;

import com.gymtracker.gym_api.domain.model.routine.Rutina;
import com.gymtracker.gym_api.infrastructure.entity.routine.RutinaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RutinaJpaRepository extends JpaRepository<RutinaEntity, UUID> {

    boolean existsByUsuarioIdAndNombre(UUID usuarioId, String nombre);

    List<RutinaEntity> findByUsuarioId(UUID usuarioId);

}
