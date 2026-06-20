package com.gymtracker.gym_api.infrastructure.jpa.exercise;

import com.gymtracker.gym_api.domain.enums.GrupoMuscular;
import com.gymtracker.gym_api.infrastructure.entity.exercise.EjercicioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EjercicioJpaRepository extends JpaRepository<EjercicioEntity, UUID> {

    List<EjercicioEntity> findByGrupoMuscularAndActivoTrueOrderByNombreAsc(GrupoMuscular grupoMuscular);

    boolean existsByNombreAndActivoTrue(String nombre);

}
