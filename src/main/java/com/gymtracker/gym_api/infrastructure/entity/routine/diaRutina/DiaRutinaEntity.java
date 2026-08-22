package com.gymtracker.gym_api.infrastructure.entity.routine.diaRutina;

import com.gymtracker.gym_api.domain.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "dias_rutina",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_dia_rutina_orden", columnNames = {"rutina_id", "orden_dia"}),
                @UniqueConstraint(name = "uk_dia_rutina_semana", columnNames = {"rutina_id", "dia_semana"})
        }
)
public class DiaRutinaEntity {

    @Id
    @Column(name = "dia_rutina_id")
    private UUID id;

    @Column(name = "rutina_id", nullable = false)
    private UUID rutinaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false, length = 20)
    private DiaSemana diaSemana;

    @Column(name = "orden_dia", nullable = false)
    private int ordenDia;

}