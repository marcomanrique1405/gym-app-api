package com.gymtracker.gym_api.controller.routine;

import com.gymtracker.gym_api.application.dto.request.routine.CreateRutinaRequest;
import com.gymtracker.gym_api.application.dto.request.routine.UpdateRutinaRequest;
import com.gymtracker.gym_api.application.dto.response.routine.RutinaResponse;
import com.gymtracker.gym_api.application.usecase.routine.rutina.CreateRutinaUseCase;
import com.gymtracker.gym_api.application.usecase.routine.rutina.DeleteRutinaUseCase;
import com.gymtracker.gym_api.application.usecase.routine.rutina.GetRutinaByIdUseCase;
import com.gymtracker.gym_api.application.usecase.routine.rutina.GetRutinasByUsuarioUseCase;
import com.gymtracker.gym_api.application.usecase.routine.rutina.UpdateRutinaUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rutinas")
public class RutinaController {

    private final CreateRutinaUseCase createRutinaUseCase;
    private final GetRutinasByUsuarioUseCase getRutinasByUsuarioUseCase;
    private final GetRutinaByIdUseCase getRutinaByIdUseCase;
    private final UpdateRutinaUseCase updateRutinaUseCase;
    private final DeleteRutinaUseCase deleteRutinaUseCase;


    @PostMapping
    public ResponseEntity<RutinaResponse> crear(@Valid @RequestBody CreateRutinaRequest request) {

        RutinaResponse response = createRutinaUseCase.crearRutina(request);

        return ResponseEntity
                .created(URI.create("/rutinas/" + response.getId()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<RutinaResponse>> obtenerMisRutinas() {
        return ResponseEntity.ok(
                getRutinasByUsuarioUseCase.obtenerRutinas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutinaResponse> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(
                getRutinaByIdUseCase.obtenerRutina(id)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RutinaResponse> actualizarRutina(@PathVariable UUID id, @Valid @RequestBody UpdateRutinaRequest request) {
        return ResponseEntity.ok(
                updateRutinaUseCase.actualizarRutina(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRutina(@PathVariable UUID id) {

        deleteRutinaUseCase.eliminarRutina(id);

        return ResponseEntity.noContent().build();

    }
}
