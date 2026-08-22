package com.gymtracker.gym_api.application.usecase.workout.serieEntrenamiento;

import com.gymtracker.gym_api.application.dto.request.workout.serieEntrenamiento.UpdateSerieEntrenamientoRequest;
import com.gymtracker.gym_api.application.dto.response.workout.serieEntrenamiento.SerieEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.model.workout.serieEntrenamiento.SerieEntrenamiento;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.domain.repository.workout.serieEntrenamiento.SerieEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoAlreadyFinishedException;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoNotFoundException;
import com.gymtracker.gym_api.shared.exception.workout.serieEntrenamiento.SerieEntrenamientoNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateSerieEntrenamientoUseCaseTest {

    @Mock private SerieEntrenamientoRepository serieEntrenamientoRepository;
    @Mock private SesionEntrenamientoRepository sesionEntrenamientoRepository;
    @Mock private SecurityUtils securityUtils;
    @InjectMocks private UpdateSerieEntrenamientoUseCase useCase;

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID sesionId = UUID.randomUUID();
    private final UUID serieId = UUID.randomUUID();
    private final UUID serieRutinaId = UUID.randomUUID();

    @Test
    void actualizaRepeticionesYPeso() {
        stubSesion(false);
        stubSerie();
        when(serieEntrenamientoRepository.save(any(SerieEntrenamiento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SerieEntrenamientoResponse response = useCase.actualizar(
                sesionId,
                serieId,
                new UpdateSerieEntrenamientoRequest(15, new BigDecimal("40.00"))
        );

        assertEquals(15, response.getRepeticionesRealizadas());
        assertEquals(new BigDecimal("40.00"), response.getPesoUtilizado());
    }

    @Test
    void actualizaParcialmenteConservandoElPeso() {
        stubSesion(false);
        stubSerie();
        when(serieEntrenamientoRepository.save(any(SerieEntrenamiento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        useCase.actualizar(
                sesionId,
                serieId,
                new UpdateSerieEntrenamientoRequest(14, null)
        );

        ArgumentCaptor<SerieEntrenamiento> captor =
                ArgumentCaptor.forClass(SerieEntrenamiento.class);
        verify(serieEntrenamientoRepository).save(captor.capture());
        assertEquals(14, captor.getValue().getRepeticionesRealizadas());
        assertEquals(new BigDecimal("25.50"), captor.getValue().getPesoUtilizado());
        assertEquals(serieRutinaId, captor.getValue().getSerieEjercicioRutinaId());
    }

    @Test
    void rechazaCuandoSesionNoExisteONoPerteneceAlUsuario() {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionEntrenamientoRepository.obtenerPorIdYUsuarioId(sesionId, usuarioId))
                .thenReturn(Optional.empty());

        assertThrows(SesionEntrenamientoNotFoundException.class,
                () -> useCase.actualizar(
                        sesionId, serieId, new UpdateSerieEntrenamientoRequest(12, null)));

        verify(serieEntrenamientoRepository, never()).save(any());
    }

    @Test
    void rechazaCuandoSesionEstaFinalizada() {
        stubSesion(true);

        assertThrows(SesionEntrenamientoAlreadyFinishedException.class,
                () -> useCase.actualizar(
                        sesionId, serieId, new UpdateSerieEntrenamientoRequest(12, null)));

        verify(serieEntrenamientoRepository, never()).save(any());
    }

    @Test
    void rechazaCuandoSerieNoExisteEnLaSesion() {
        stubSesion(false);
        when(serieEntrenamientoRepository
                .obtenerPorIdYSesionEntrenamientoId(serieId, sesionId))
                .thenReturn(Optional.empty());

        assertThrows(SerieEntrenamientoNotFoundException.class,
                () -> useCase.actualizar(
                        sesionId, serieId, new UpdateSerieEntrenamientoRequest(12, null)));

        verify(serieEntrenamientoRepository, never()).save(any());
    }

    private void stubSesion(boolean finalizada) {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionEntrenamientoRepository.obtenerPorIdYUsuarioId(sesionId, usuarioId))
                .thenReturn(Optional.of(SesionEntrenamiento.builder()
                        .id(sesionId)
                        .usuarioId(usuarioId)
                        .finalizada(finalizada)
                        .build()));
    }

    private void stubSerie() {
        when(serieEntrenamientoRepository
                .obtenerPorIdYSesionEntrenamientoId(serieId, sesionId))
                .thenReturn(Optional.of(SerieEntrenamiento.builder()
                        .id(serieId)
                        .sesionEntrenamientoId(sesionId)
                        .serieEjercicioRutinaId(serieRutinaId)
                        .repeticionesRealizadas(10)
                        .pesoUtilizado(new BigDecimal("25.50"))
                        .build()));
    }
}
