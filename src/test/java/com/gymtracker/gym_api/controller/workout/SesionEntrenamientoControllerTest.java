package com.gymtracker.gym_api.controller.workout;

import com.gymtracker.gym_api.application.dto.response.PageResponse;
import com.gymtracker.gym_api.application.dto.response.workout.SesionEntrenamientoResponse;
import com.gymtracker.gym_api.application.usecase.workout.FinalizarSesionEntrenamientoUseCase;
import com.gymtracker.gym_api.application.usecase.workout.GetHistorialSesionesEntrenamientoUseCase;
import com.gymtracker.gym_api.application.usecase.workout.GetSesionEntrenamientoActivaUseCase;
import com.gymtracker.gym_api.application.usecase.workout.IniciarSesionEntrenamientoUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SesionEntrenamientoControllerTest {

    @Mock private IniciarSesionEntrenamientoUseCase iniciarSesionEntrenamientoUseCase;
    @Mock private GetSesionEntrenamientoActivaUseCase getSesionEntrenamientoActivaUseCase;
    @Mock private FinalizarSesionEntrenamientoUseCase finalizarSesionEntrenamientoUseCase;
    @Mock private GetHistorialSesionesEntrenamientoUseCase getHistorialSesionesEntrenamientoUseCase;
    @InjectMocks private SesionEntrenamientoController controller;

    @Test
    void delegaLosParametrosDePaginacionYDevuelveLaPagina() {
        PageResponse<SesionEntrenamientoResponse> page =
                new PageResponse<>(List.of(), 2, 15, 31, 3, false, true);
        when(getHistorialSesionesEntrenamientoUseCase.obtenerHistorial(2, 15)).thenReturn(page);

        var response = controller.obtenerHistorial(2, 15);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(page, response.getBody());
        verify(getHistorialSesionesEntrenamientoUseCase).obtenerHistorial(2, 15);
    }
}
