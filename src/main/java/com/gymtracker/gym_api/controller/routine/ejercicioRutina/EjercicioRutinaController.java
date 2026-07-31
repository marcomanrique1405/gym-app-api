package com.gymtracker.gym_api.controller.routine.ejercicioRutina;

import com.gymtracker.gym_api.application.dto.request.routine.ejercicioRutina.EjercicioRutinaRequest;
import com.gymtracker.gym_api.application.dto.request.routine.ejercicioRutina.UpdateEjercicioRutinaRequest;
import com.gymtracker.gym_api.application.dto.response.routine.ejercicioRutina.EjercicioRutinaResponse;
import com.gymtracker.gym_api.application.usecase.routine.ejercicioRutina.CreateEjercicioRutinaUseCase;
import com.gymtracker.gym_api.application.usecase.routine.ejercicioRutina.DeleteEjercicioRutinaUseCase;
import com.gymtracker.gym_api.application.usecase.routine.ejercicioRutina.UpdateEjercicioRutinaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/rutinas/{rutinaId}/ejercicios-rutina")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class EjercicioRutinaController {

    private final CreateEjercicioRutinaUseCase createEjercicioRutinaUseCase;
    private final UpdateEjercicioRutinaUseCase updateEjercicioRutinaUseCase;
    private final DeleteEjercicioRutinaUseCase deleteEjercicioRutinaUseCase;

    public EjercicioRutinaController(
            CreateEjercicioRutinaUseCase createEjercicioRutinaUseCase,
            UpdateEjercicioRutinaUseCase updateEjercicioRutinaUseCase,
            DeleteEjercicioRutinaUseCase deleteEjercicioRutinaUseCase
    ) {
        this.createEjercicioRutinaUseCase = createEjercicioRutinaUseCase;
        this.updateEjercicioRutinaUseCase = updateEjercicioRutinaUseCase;
        this.deleteEjercicioRutinaUseCase = deleteEjercicioRutinaUseCase;
    }

    @PostMapping
    public ResponseEntity<EjercicioRutinaResponse> crear(
            @PathVariable UUID rutinaId,
            @Valid @RequestBody EjercicioRutinaRequest request
    ) {
        EjercicioRutinaResponse response = createEjercicioRutinaUseCase.crear(
                rutinaId,
                request
        );

        URI location = URI.create(
                "/rutinas/" + rutinaId +
                        "/ejercicios-rutina/" + response.getId()
        );

        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/{ejercicioRutinaId}")
    public ResponseEntity<EjercicioRutinaResponse> actualizar(
            @PathVariable UUID rutinaId,
            @PathVariable UUID ejercicioRutinaId,
            @Valid @RequestBody UpdateEjercicioRutinaRequest request
    ) {
        EjercicioRutinaResponse response = updateEjercicioRutinaUseCase.update(
                rutinaId,
                ejercicioRutinaId,
                request
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{ejercicioRutinaId}/dias/{diaRutinaId}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID rutinaId,
            @PathVariable UUID diaRutinaId,
            @PathVariable UUID ejercicioRutinaId
    ) {
        deleteEjercicioRutinaUseCase.eliminar(
                rutinaId,
                diaRutinaId,
                ejercicioRutinaId
        );

        return ResponseEntity.noContent().build();
    }
}
