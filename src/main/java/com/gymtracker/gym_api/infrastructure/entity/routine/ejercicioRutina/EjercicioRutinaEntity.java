package com.gymtracker.gym_api.infrastructure.entity.routine.ejercicioRutina;

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
@Table(name = "ejercicios_rutina")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EjercicioRutinaEntity {

    @Id
    @Column(name = "ejercicio_rutina_id", nullable = false)
    private UUID id;

    @Column(name = "dia_rutina_id", nullable = false)
    private UUID diaRutinaId;

    @Column(name = "ejercicio_id", nullable = false)
    private UUID ejercicioId;

    @Column(nullable = false)
    private int orden;

    @Column(name = "peso_objetivo", nullable = false, precision = 6, scale = 2)
    private BigDecimal pesoObjetivo;

    @Column(name = "incremento_peso", nullable = false, precision = 4, scale = 2)
    private BigDecimal incrementoPeso;

    @Column(name = "sobrecarga_activa", nullable = false)
    private boolean sobrecargaActiva;

    @Column(nullable = false)
    private boolean activo;
}