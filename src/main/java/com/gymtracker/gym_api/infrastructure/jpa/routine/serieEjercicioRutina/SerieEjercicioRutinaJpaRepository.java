package com.gymtracker.gym_api.infrastructure.jpa.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.infrastructure.entity.routine.serieEjercicioRutina.SerieEjercicioRutinaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    @Query(value = """
            select exists (
              select 1 from series_rutina sr
              join ejercicios_rutina er on er.ejercicio_rutina_id = sr.ejercicio_rutina_id
              join dias_rutina dr on dr.dia_rutina_id = er.dia_rutina_id
              join rutinas r on r.rutina_id = dr.rutina_id
              where sr.serie_rutina_id = :serieId and r.rutina_id = :rutinaId
                and sr.activo = true and er.activo = true and r.activa = true
            )
            """, nativeQuery = true)
    boolean perteneceActivaARutina(UUID serieId, UUID rutinaId);
}
