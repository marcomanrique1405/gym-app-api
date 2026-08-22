package com.gymtracker.gym_api.infrastructure.entity.workout;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sesion_entrenamiento")
public class SesionEntrenamientoEntity {

    @Id
    @Column(name = "sesion_entrenamiento_id")
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "rutina_id", nullable = false)
    private UUID rutinaId;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "finalizada", nullable = false)
    private boolean finalizada;

}
