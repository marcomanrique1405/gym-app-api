package com.gymtracker.gym_api.infrastructure.jpa.routine.rutina;

import com.gymtracker.gym_api.infrastructure.entity.routine.rutina.RutinaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RutinaJpaRepository extends JpaRepository<RutinaEntity, UUID> {

    boolean existsByUsuarioIdAndNombreIgnoreCaseAndActivaTrue(UUID usuarioId, String nombre);

    List<RutinaEntity> findByUsuarioIdAndActivaTrue(UUID usuarioId);

    Optional<RutinaEntity> findByIdAndUsuarioIdAndActivaTrue(UUID id, UUID usuarioId);

}
