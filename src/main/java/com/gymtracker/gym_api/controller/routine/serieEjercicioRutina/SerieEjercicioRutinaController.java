package com.gymtracker.gym_api.controller.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.application.dto.request.routine.serieEjercicioRutina.SerieEjercicioRutinaRequest;
import com.gymtracker.gym_api.application.dto.request.routine.serieEjercicioRutina.UpdateSerieEjercicioRutinaRequest;
import com.gymtracker.gym_api.application.dto.response.routine.serieEjercicioRutina.SerieEjercicioRutinaResponse;
import com.gymtracker.gym_api.application.usecase.routine.serieEjercicioRutina.CreateSerieEjercicioRutinaUseCase;
import com.gymtracker.gym_api.application.usecase.routine.serieEjercicioRutina.DeleteSerieEjercicioRutinaUseCase;
import com.gymtracker.gym_api.application.usecase.routine.serieEjercicioRutina.GetSeriesEjercicioRutinaUseCase;
import com.gymtracker.gym_api.application.usecase.routine.serieEjercicioRutina.UpdateSerieEjercicioRutinaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rutinas/{rutinaId}/dias/{diaRutinaId}/ejercicios-rutina/{ejercicioRutinaId}/series")
public class SerieEjercicioRutinaController {

    private final CreateSerieEjercicioRutinaUseCase createSerieEjercicioRutinaUseCase;
    private final GetSeriesEjercicioRutinaUseCase getSeriesEjercicioRutinaUseCase;
    private final UpdateSerieEjercicioRutinaUseCase updateSerieEjercicioRutinaUseCase;
    private final DeleteSerieEjercicioRutinaUseCase deleteSerieEjercicioRutinaUseCase;

    public SerieEjercicioRutinaController(
            CreateSerieEjercicioRutinaUseCase createSerieEjercicioRutinaUseCase,
            GetSeriesEjercicioRutinaUseCase getSeriesEjercicioRutinaUseCase,
            UpdateSerieEjercicioRutinaUseCase updateSerieEjercicioRutinaUseCase,
            DeleteSerieEjercicioRutinaUseCase deleteSerieEjercicioRutinaUseCase
    ) {
        this.createSerieEjercicioRutinaUseCase = createSerieEjercicioRutinaUseCase;
        this.getSeriesEjercicioRutinaUseCase = getSeriesEjercicioRutinaUseCase;
        this.updateSerieEjercicioRutinaUseCase = updateSerieEjercicioRutinaUseCase;
        this.deleteSerieEjercicioRutinaUseCase = deleteSerieEjercicioRutinaUseCase;
    }

    @PostMapping
    public ResponseEntity<SerieEjercicioRutinaResponse> crear(
            @PathVariable UUID rutinaId,
            @PathVariable UUID diaRutinaId,
            @PathVariable UUID ejercicioRutinaId,
            @Valid @RequestBody SerieEjercicioRutinaRequest request
    ) {
        SerieEjercicioRutinaResponse response = createSerieEjercicioRutinaUseCase.crear(
                rutinaId,
                diaRutinaId,
                ejercicioRutinaId,
                request
        );

        URI location = URI.create(
                "/rutinas/" + rutinaId
                        + "/dias/" + diaRutinaId
                        + "/ejercicios-rutina/" + ejercicioRutinaId
                        + "/series/" + response.getId()
        );

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SerieEjercicioRutinaResponse>> obtenerSeries(
            @PathVariable UUID rutinaId,
            @PathVariable UUID diaRutinaId,
            @PathVariable UUID ejercicioRutinaId
    ) {
        return ResponseEntity.ok(
                getSeriesEjercicioRutinaUseCase.obtenerSeriesPorEjercicioRutina(
                        rutinaId,
                        diaRutinaId,
                        ejercicioRutinaId
                )
        );
    }

    @PatchMapping("/{serieEjercicioRutinaId}")
    public ResponseEntity<SerieEjercicioRutinaResponse> actualizar(
            @PathVariable UUID rutinaId,
            @PathVariable UUID diaRutinaId,
            @PathVariable UUID ejercicioRutinaId,
            @PathVariable UUID serieEjercicioRutinaId,
            @Valid @RequestBody UpdateSerieEjercicioRutinaRequest request
    ) {
        return ResponseEntity.ok(
                updateSerieEjercicioRutinaUseCase.update(
                        rutinaId,
                        diaRutinaId,
                        ejercicioRutinaId,
                        serieEjercicioRutinaId,
                        request
                )
        );
    }

    @DeleteMapping("/{serieEjercicioRutinaId}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID rutinaId,
            @PathVariable UUID diaRutinaId,
            @PathVariable UUID ejercicioRutinaId,
            @PathVariable UUID serieEjercicioRutinaId
    ) {
        deleteSerieEjercicioRutinaUseCase.eliminar(
                rutinaId,
                diaRutinaId,
                ejercicioRutinaId,
                serieEjercicioRutinaId
        );

        return ResponseEntity.noContent().build();
    }
}
