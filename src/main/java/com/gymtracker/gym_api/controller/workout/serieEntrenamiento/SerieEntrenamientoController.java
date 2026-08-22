package com.gymtracker.gym_api.controller.workout.serieEntrenamiento;

import com.gymtracker.gym_api.application.dto.request.workout.serieEntrenamiento.SerieEntrenamientoRequest;
import com.gymtracker.gym_api.application.dto.request.workout.serieEntrenamiento.UpdateSerieEntrenamientoRequest;
import com.gymtracker.gym_api.application.dto.response.workout.serieEntrenamiento.SerieEntrenamientoResponse;
import com.gymtracker.gym_api.application.usecase.workout.serieEntrenamiento.CreateSerieEntrenamientoUseCase;
import com.gymtracker.gym_api.application.usecase.workout.serieEntrenamiento.GetSeriesEntrenamientoUseCase;
import com.gymtracker.gym_api.application.usecase.workout.serieEntrenamiento.UpdateSerieEntrenamientoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sesiones-entrenamiento/{sesionEntrenamientoId}/series")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class SerieEntrenamientoController {

    private final CreateSerieEntrenamientoUseCase createSerieEntrenamientoUseCase;
    private final GetSeriesEntrenamientoUseCase getSeriesEntrenamientoUseCase;
    private final UpdateSerieEntrenamientoUseCase updateSerieEntrenamientoUseCase;

    public SerieEntrenamientoController(
            CreateSerieEntrenamientoUseCase createSerieEntrenamientoUseCase,
            GetSeriesEntrenamientoUseCase getSeriesEntrenamientoUseCase,
            UpdateSerieEntrenamientoUseCase updateSerieEntrenamientoUseCase
    ) {
        this.createSerieEntrenamientoUseCase = createSerieEntrenamientoUseCase;
        this.getSeriesEntrenamientoUseCase = getSeriesEntrenamientoUseCase;
        this.updateSerieEntrenamientoUseCase = updateSerieEntrenamientoUseCase;
    }

    @PostMapping
    public ResponseEntity<SerieEntrenamientoResponse> crear(
            @PathVariable UUID sesionEntrenamientoId,
            @Valid @RequestBody SerieEntrenamientoRequest request
    ) {
        SerieEntrenamientoResponse response = createSerieEntrenamientoUseCase
                .crear(sesionEntrenamientoId, request);

        URI location = URI.create(
                "/sesiones-entrenamiento/" + sesionEntrenamientoId
                        + "/series/" + response.getId()
        );

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SerieEntrenamientoResponse>> obtener(
            @PathVariable UUID sesionEntrenamientoId
    ) {
        return ResponseEntity.ok(
                getSeriesEntrenamientoUseCase.obtenerPorSesion(sesionEntrenamientoId)
        );
    }

    @PatchMapping("/{serieEntrenamientoId}")
    public ResponseEntity<SerieEntrenamientoResponse> actualizar(
            @PathVariable UUID sesionEntrenamientoId,
            @PathVariable UUID serieEntrenamientoId,
            @Valid @RequestBody UpdateSerieEntrenamientoRequest request
    ) {
        return ResponseEntity.ok(
                updateSerieEntrenamientoUseCase.actualizar(
                        sesionEntrenamientoId,
                        serieEntrenamientoId,
                        request
                )
        );
    }
}
