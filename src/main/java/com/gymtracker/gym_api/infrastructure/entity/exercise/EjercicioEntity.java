package com.gymtracker.gym_api.infrastructure.entity.exercise;

import com.gymtracker.gym_api.domain.enums.GrupoMuscular;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "ejercicios")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EjercicioEntity {

    @Id
    @Column(name = "ejercicio_id", nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "grupo_muscular", nullable = false, length = 50)
    private GrupoMuscular grupoMuscular;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo;
}