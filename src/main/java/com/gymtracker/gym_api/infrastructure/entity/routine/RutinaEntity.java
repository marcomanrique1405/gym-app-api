package com.gymtracker.gym_api.infrastructure.entity.routine;

import com.gymtracker.gym_api.domain.enums.TipoProgresion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "rutinas",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_rutina_usuario_nombre", columnNames = {"usuario_id", "nombre"})
        }
)
public class RutinaEntity {

    @Column(name = "rutina_id", nullable = false)
    @Id
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_progresion", length = 20, nullable = false)
    private TipoProgresion tipoProgresion;

    @Column(nullable = false)
    private Boolean activa;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}
