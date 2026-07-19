package com.gymtracker.gym_api.controller.workout;

import com.gymtracker.gym_api.application.dto.response.workout.SesionEntrenamientoResponse;
import com.gymtracker.gym_api.application.usecase.workout.FinalizarSesionEntrenamientoUseCase;
import com.gymtracker.gym_api.application.usecase.workout.GetHistorialSesionesEntrenamientoUseCase;
import com.gymtracker.gym_api.application.usecase.workout.GetSesionEntrenamientoActivaUseCase;
import com.gymtracker.gym_api.application.usecase.workout.IniciarSesionEntrenamientoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sesiones-entrenamiento")
public class SesionEntrenamientoController {

    private final IniciarSesionEntrenamientoUseCase iniciarSesionEntrenamientoUseCase;
    private final GetSesionEntrenamientoActivaUseCase getSesionEntrenamientoActivaUseCase;
    private final FinalizarSesionEntrenamientoUseCase finalizarSesionEntrenamientoUseCase;
    private final GetHistorialSesionesEntrenamientoUseCase getHistorialSesionesEntrenamientoUseCase;

    @PostMapping("/rutinas/{rutinaId}")
    public ResponseEntity<SesionEntrenamientoResponse> iniciar(@PathVariable UUID rutinaId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(iniciarSesionEntrenamientoUseCase.iniciarSesion(rutinaId));
    }

    @GetMapping("/activa")
    public ResponseEntity<SesionEntrenamientoResponse> obtenerActiva() {
        return ResponseEntity.ok(
                getSesionEntrenamientoActivaUseCase.obtenerSesionActiva()
        );
    }

    @PatchMapping("/{sesionEntrenamientoId}/finalizar")
    public ResponseEntity<SesionEntrenamientoResponse> finalizar(
            @PathVariable UUID sesionEntrenamientoId
    ) {
        return ResponseEntity.ok(
                finalizarSesionEntrenamientoUseCase.finalizarSesion(sesionEntrenamientoId)
        );
    }

    @GetMapping
    public ResponseEntity<List<SesionEntrenamientoResponse>> obtenerHistorial() {
        return ResponseEntity.ok(
                getHistorialSesionesEntrenamientoUseCase.obtenerHistorial()
        );
    }

}
