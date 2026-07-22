package com.gymtracker.gym_api.controller.workout.serieEntrenamiento;

import com.gymtracker.gym_api.application.dto.request.workout.serieEntrenamiento.SerieEntrenamientoRequest;
import com.gymtracker.gym_api.application.dto.request.workout.serieEntrenamiento.UpdateSerieEntrenamientoRequest;
import com.gymtracker.gym_api.application.dto.response.workout.serieEntrenamiento.SerieEntrenamientoResponse;
import com.gymtracker.gym_api.application.usecase.workout.serieEntrenamiento.CreateSerieEntrenamientoUseCase;
import com.gymtracker.gym_api.application.usecase.workout.serieEntrenamiento.GetSeriesEntrenamientoUseCase;
import com.gymtracker.gym_api.application.usecase.workout.serieEntrenamiento.UpdateSerieEntrenamientoUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SerieEntrenamientoControllerTest {

    @Mock private CreateSerieEntrenamientoUseCase createUseCase;
    @Mock private GetSeriesEntrenamientoUseCase getUseCase;
    @Mock private UpdateSerieEntrenamientoUseCase updateUseCase;
    @InjectMocks private SerieEntrenamientoController controller;

    private final UUID sesionId = UUID.randomUUID();
    private final UUID serieId = UUID.randomUUID();
    private final UUID serieRutinaId = UUID.randomUUID();

    @Test
    void postCreaSerieYDevuelveCreatedConLocation() {
        SerieEntrenamientoRequest request = SerieEntrenamientoRequest.builder()
                .serieEjercicioRutinaId(serieRutinaId)
                .repeticionesRealizadas(10)
                .pesoUtilizado(new BigDecimal("25.00"))
                .build();
        SerieEntrenamientoResponse body = response();
        when(createUseCase.crear(sesionId, request)).thenReturn(body);

        ResponseEntity<SerieEntrenamientoResponse> response =
                controller.crear(sesionId, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(body, response.getBody());
        assertEquals(
                URI.create("/sesiones-entrenamiento/" + sesionId + "/series/" + serieId),
                response.getHeaders().getLocation()
        );
    }

    @Test
    void getDevuelveLasSeriesDeLaSesion() {
        List<SerieEntrenamientoResponse> body = List.of(response());
        when(getUseCase.obtenerPorSesion(sesionId)).thenReturn(body);

        ResponseEntity<List<SerieEntrenamientoResponse>> response =
                controller.obtener(sesionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(body, response.getBody());
        verify(getUseCase).obtenerPorSesion(sesionId);
    }

    @Test
    void patchActualizaYDevuelveLaSerie() {
        UpdateSerieEntrenamientoRequest request =
                new UpdateSerieEntrenamientoRequest(12, new BigDecimal("30.00"));
        SerieEntrenamientoResponse body = response();
        when(updateUseCase.actualizar(sesionId, serieId, request)).thenReturn(body);

        ResponseEntity<SerieEntrenamientoResponse> response =
                controller.actualizar(sesionId, serieId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(body, response.getBody());
        verify(updateUseCase).actualizar(sesionId, serieId, request);
    }

    private SerieEntrenamientoResponse response() {
        return SerieEntrenamientoResponse.builder()
                .id(serieId)
                .sesionEntrenamientoId(sesionId)
                .serieEjercicioRutinaId(serieRutinaId)
                .repeticionesRealizadas(10)
                .pesoUtilizado(new BigDecimal("25.00"))
                .build();
    }
}
