package com.gymtracker.gym_api.infrastructure.entity.workout.serieEntrenamiento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "series_entrenamiento")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SerieEntrenamientoEntity {

    @Id
    @Column(name = "serie_entrenamiento_id", nullable = false)
    private UUID id;

    @Column(name = "sesion_entrenamiento_id", nullable = false)
    private UUID sesionEntrenamientoId;

    @Column(name = "serie_rutina_id", nullable = false)
    private UUID serieEjercicioRutinaId;

    @Column(name = "repeticiones_realizadas", nullable = false)
    private Integer repeticionesRealizadas;

    @Column(name = "peso_utilizado", nullable = false, precision = 10, scale = 2)
    private BigDecimal pesoUtilizado;
}
