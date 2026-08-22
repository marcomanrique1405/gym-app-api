package com.gymtracker.gym_api.infrastructure.entity.routine.serieEjercicioRutina;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "series_rutina")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SerieEjercicioRutinaEntity {

    @Id
    @Column(name = "serie_rutina_id", nullable = false)
    private UUID id;

    @Column(name = "ejercicio_rutina_id", nullable = false)
    private UUID ejercicioRutinaId;

    @Column(name = "orden", nullable = false)
    private int orden;

    @Column(name = "repeticiones_min", nullable = false)
    private int repeticionesMin;

    @Column(name = "repeticiones_max", nullable = false)
    private int repeticionesMax;

    @Column(name = "activo", nullable = false)
    private boolean activo;
}
