package com.gymtracker.gym_api.controller.routine.diaRutina;

import com.gymtracker.gym_api.application.dto.request.routine.diaRutina.DiaRutinaRequest;
import com.gymtracker.gym_api.application.dto.request.routine.diaRutina.UpdateDiaRutinaRequest;
import com.gymtracker.gym_api.application.dto.response.routine.diaRutina.DiaRutinaResponse;
import com.gymtracker.gym_api.application.usecase.routine.diaRutina.CreateDiaRutinaUseCase;
import com.gymtracker.gym_api.application.usecase.routine.diaRutina.DeleteDiaRutinaUseCase;
import com.gymtracker.gym_api.application.usecase.routine.diaRutina.GetDiasRutinaUseCase;
import com.gymtracker.gym_api.application.usecase.routine.diaRutina.UpdateDiaRutinaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rutinas/{rutinaId}/dias")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class DiaRutinaController {

    private final CreateDiaRutinaUseCase createDiaRutinaUseCase;
    private final GetDiasRutinaUseCase getDiasRutinaUseCase;
    private final UpdateDiaRutinaUseCase updateDiaRutinaUseCase;
    private final DeleteDiaRutinaUseCase deleteDiaRutinaUseCase;

    public DiaRutinaController(
            CreateDiaRutinaUseCase createDiaRutinaUseCase,
            GetDiasRutinaUseCase getDiasRutinaUseCase,
            UpdateDiaRutinaUseCase updateDiaRutinaUseCase, DeleteDiaRutinaUseCase deleteDiaRutinaUseCase
    ) {
            this.createDiaRutinaUseCase = createDiaRutinaUseCase;
            this.getDiasRutinaUseCase = getDiasRutinaUseCase;
            this.updateDiaRutinaUseCase = updateDiaRutinaUseCase;
            this.deleteDiaRutinaUseCase = deleteDiaRutinaUseCase;
    }

    @PostMapping
    public ResponseEntity<DiaRutinaResponse> crear(@PathVariable UUID rutinaId,@Valid @RequestBody DiaRutinaRequest request) {

        DiaRutinaResponse response = createDiaRutinaUseCase.crear(rutinaId, request);

        URI location = URI.create("/rutinas/" + rutinaId + "/dias/" + response.getId());

        return ResponseEntity
                .created(location)
                .body(response);

    }

    @GetMapping
    public ResponseEntity<List<DiaRutinaResponse>> obtenerDiasRutina(@PathVariable UUID rutinaId) {
        return ResponseEntity.ok(
                getDiasRutinaUseCase.obtenerDiasPorRutina(rutinaId)
        );
    }


    @PatchMapping("/{diaRutinaId}")
    public ResponseEntity<DiaRutinaResponse> actualizarDiaRutina(@PathVariable UUID rutinaId, @PathVariable UUID diaRutinaId, @Valid @RequestBody UpdateDiaRutinaRequest request) {
        return ResponseEntity.ok(
                updateDiaRutinaUseCase.update(rutinaId, diaRutinaId, request)
        );
    }

    @DeleteMapping("/{diaRutinaId}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID rutinaId,@PathVariable UUID diaRutinaId) {
        deleteDiaRutinaUseCase.delet(rutinaId, diaRutinaId);
        return ResponseEntity.noContent().build();
    }

}
