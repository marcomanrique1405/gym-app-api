package com.gymtracker.gym_api.application.usecase.workout.serieEntrenamiento;

import com.gymtracker.gym_api.application.dto.response.workout.serieEntrenamiento.SerieEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.model.workout.serieEntrenamiento.SerieEntrenamiento;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.domain.repository.workout.serieEntrenamiento.SerieEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSeriesEntrenamientoUseCaseTest {

    @Mock private SerieEntrenamientoRepository serieEntrenamientoRepository;
    @Mock private SesionEntrenamientoRepository sesionEntrenamientoRepository;
    @Mock private SecurityUtils securityUtils;
    @InjectMocks private GetSeriesEntrenamientoUseCase useCase;

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID sesionId = UUID.randomUUID();

    @Test
    void listaLasSeriesDeLaSesion() {
        UUID serieId = UUID.randomUUID();
        UUID serieRutinaId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionEntrenamientoRepository.obtenerPorIdYUsuarioId(sesionId, usuarioId))
                .thenReturn(Optional.of(SesionEntrenamiento.builder()
                        .id(sesionId).usuarioId(usuarioId).build()));
        when(serieEntrenamientoRepository.obtenerPorSesionEntrenamientoId(sesionId))
                .thenReturn(List.of(SerieEntrenamiento.builder()
                        .id(serieId)
                        .sesionEntrenamientoId(sesionId)
                        .serieEjercicioRutinaId(serieRutinaId)
                        .repeticionesRealizadas(12)
                        .pesoUtilizado(new BigDecimal("30.00"))
                        .build()));

        List<SerieEntrenamientoResponse> response = useCase.obtenerPorSesion(sesionId);

        assertEquals(1, response.size());
        assertEquals(serieId, response.get(0).getId());
        assertEquals(12, response.get(0).getRepeticionesRealizadas());
    }

    @Test
    void rechazaCuandoSesionNoExisteONoPerteneceAlUsuario() {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionEntrenamientoRepository.obtenerPorIdYUsuarioId(sesionId, usuarioId))
                .thenReturn(Optional.empty());

        assertThrows(SesionEntrenamientoNotFoundException.class,
                () -> useCase.obtenerPorSesion(sesionId));

        verify(serieEntrenamientoRepository, never())
                .obtenerPorSesionEntrenamientoId(sesionId);
    }
}
