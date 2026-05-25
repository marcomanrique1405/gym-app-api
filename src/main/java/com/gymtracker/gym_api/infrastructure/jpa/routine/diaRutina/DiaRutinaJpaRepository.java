package com.gymtracker.gym_api.infrastructure.jpa.routine.diaRutina;

import com.gymtracker.gym_api.domain.enums.DiaSemana;
import com.gymtracker.gym_api.infrastructure.entity.routine.diaRutina.DiaRutinaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiaRutinaJpaRepository extends JpaRepository<DiaRutinaEntity, UUID> {

    boolean existsByRutinaIdAndDiaSemana(UUID rutinaId, DiaSemana diaSemana);

    boolean existsByRutinaIdAndOrdenDia(UUID rutinaId, Integer ordenDia);

    List<DiaRutinaEntity> findByRutinaIdOrderByOrdenDiaAsc(UUID rutinaId);

    Optional<DiaRutinaEntity> findByIdAndRutinaId(UUID id, UUID rutinaId);

    int countByRutinaId(UUID rutinaId);

}