package com.gymtracker.gym_api.controller.exercise;

import com.gymtracker.gym_api.application.dto.request.exercise.EjercicioRequest;
import com.gymtracker.gym_api.application.dto.request.exercise.UpdateEjercicioRequest;
import com.gymtracker.gym_api.application.dto.response.exercise.EjercicioResponse;
import com.gymtracker.gym_api.application.usecase.exercise.CreateEjercicioUseCase;
import com.gymtracker.gym_api.application.usecase.exercise.GetEjerciciosPorGrupoMuscularUseCase;
import com.gymtracker.gym_api.application.usecase.exercise.UpdateEjercicioUseCase;
import com.gymtracker.gym_api.domain.enums.GrupoMuscular;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ejercicio")
public class EjercicioController {

    private final CreateEjercicioUseCase createEjercicioUseCase;
    private final GetEjerciciosPorGrupoMuscularUseCase getEjerciciosPorGrupoMuscular;
    private final UpdateEjercicioUseCase updateEjercicioUseCase;

    public EjercicioController(CreateEjercicioUseCase createEjercicioUseCase, GetEjerciciosPorGrupoMuscularUseCase getEjerciciosPorGrupoMuscular, UpdateEjercicioUseCase updateEjercicioUseCase) {
        this.createEjercicioUseCase = createEjercicioUseCase;
        this.getEjerciciosPorGrupoMuscular = getEjerciciosPorGrupoMuscular;
        this.updateEjercicioUseCase = updateEjercicioUseCase;
    }

    @PostMapping
    public ResponseEntity<EjercicioResponse> crear(@Valid @RequestBody EjercicioRequest request) {
        EjercicioResponse response = createEjercicioUseCase.guardar(request);

        URI location = URI.create("/ejercicio/" + response.getId());

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping("/{grupoMuscular}")
    public ResponseEntity<List<EjercicioResponse>> obtenerPorGrupoMuscular(@PathVariable GrupoMuscular grupoMuscular) {
        return ResponseEntity.ok(
                getEjerciciosPorGrupoMuscular.obtnerEjerciciosPorGrupoMuscular(grupoMuscular)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EjercicioResponse> actualizarEjercicio(@PathVariable UUID id,@RequestBody UpdateEjercicioRequest request) {
        return ResponseEntity.ok(
                updateEjercicioUseCase.actualiazarEjercicio(id, request)
        );
    }

}
