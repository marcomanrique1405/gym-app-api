package com.gymtracker.gym_api.controller.routine;

import com.gymtracker.gym_api.application.dto.request.routine.CreateRutinaRequest;
import com.gymtracker.gym_api.application.dto.response.routine.CreateRutinaResponse;
import com.gymtracker.gym_api.application.usecase.routine.CreateRutinaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/rutinas")
public class RutinaController {

    private final CreateRutinaUseCase createRutinaUseCase;


    public RutinaController(CreateRutinaUseCase createRutinaUseCase) {
        this.createRutinaUseCase = createRutinaUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateRutinaResponse> crear(@Valid @RequestBody CreateRutinaRequest request) {

        CreateRutinaResponse response = createRutinaUseCase.crearRutina(request);

        return ResponseEntity
                .created(URI.create("/rutinas/" + response.getId()))
                .body(response);
    }

}
