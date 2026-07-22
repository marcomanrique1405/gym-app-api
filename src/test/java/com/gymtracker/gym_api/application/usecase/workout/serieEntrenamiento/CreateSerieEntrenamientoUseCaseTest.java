package com.gymtracker.gym_api.application.usecase.workout.serieEntrenamiento;

import com.gymtracker.gym_api.application.dto.request.workout.serieEntrenamiento.SerieEntrenamientoRequest;
import com.gymtracker.gym_api.application.dto.response.workout.serieEntrenamiento.SerieEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.routine.serieEjercicioRutina.SerieEjercicioRutina;
import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.model.workout.serieEntrenamiento.SerieEntrenamiento;
import com.gymtracker.gym_api.domain.repository.routine.serieEjercicioRutina.SerieEjercicioRutinaRepository;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.domain.repository.workout.serieEntrenamiento.SerieEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina.SerieEjercicioRutinaNotFoundException;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoAlreadyFinishedException;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoNotFoundException;
import com.gymtracker.gym_api.shared.exception.workout.serieEntrenamiento.SerieEntrenamientoAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class CreateSerieEntrenamientoUseCaseTest {

    @Mock private SerieEntrenamientoRepository serieEntrenamientoRepository;
    @Mock private SesionEntrenamientoRepository sesionEntrenamientoRepository;
    @Mock private SerieEjercicioRutinaRepository serieEjercicioRutinaRepository;
    @Mock private SecurityUtils securityUtils;
    @InjectMocks private CreateSerieEntrenamientoUseCase useCase;

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID sesionId = UUID.randomUUID();
    private final UUID rutinaId = UUID.randomUUID();
    private final UUID serieRutinaId = UUID.randomUUID();

    @Test
    void creaSerieCorrectamente() {
        stubSesion(false);
        when(serieEjercicioRutinaRepository.obtenerActivaPorId(serieRutinaId))
                .thenReturn(Optional.of(serieRutina()));
        when(serieEntrenamientoRepository
                .existePorSesionEntrenamientoIdYSerieEjercicioRutinaId(sesionId, serieRutinaId))
                .thenReturn(false);
        when(serieEntrenamientoRepository.save(any(SerieEntrenamiento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SerieEntrenamientoResponse response = useCase.crear(sesionId, request());

        assertEquals(sesionId, response.getSesionEntrenamientoId());
        assertEquals(serieRutinaId, response.getSerieEjercicioRutinaId());
        assertEquals(10, response.getRepeticionesRealizadas());
        assertEquals(new BigDecimal("25.50"), response.getPesoUtilizado());
        verify(serieEntrenamientoRepository).save(any(SerieEntrenamiento.class));
    }

    @Test
    void rechazaCuandoSesionNoExisteONoPerteneceAlUsuario() {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionEntrenamientoRepository.obtenerPorIdYUsuarioId(sesionId, usuarioId))
                .thenReturn(Optional.empty());

        assertThrows(SesionEntrenamientoNotFoundException.class,
                () -> useCase.crear(sesionId, request()));

        verify(serieEntrenamientoRepository, never()).save(any());
    }

    @Test
    void rechazaCuandoSesionEstaFinalizada() {
        stubSesion(true);

        assertThrows(SesionEntrenamientoAlreadyFinishedException.class,
                () -> useCase.crear(sesionId, request()));

        verify(serieEntrenamientoRepository, never()).save(any());
    }

    @Test
    void rechazaCuandoSerieDeRutinaNoExisteONoEstaActiva() {
        stubSesion(false);
        when(serieEjercicioRutinaRepository.obtenerActivaPorId(serieRutinaId))
                .thenReturn(Optional.empty());

        assertThrows(SerieEjercicioRutinaNotFoundException.class,
                () -> useCase.crear(sesionId, request()));

        verify(serieEntrenamientoRepository, never()).save(any());
    }

    @Test
    void rechazaCuandoSerieYaFueRegistradaEnLaSesion() {
        stubSesion(false);
        when(serieEjercicioRutinaRepository.obtenerActivaPorId(serieRutinaId))
                .thenReturn(Optional.of(serieRutina()));
        when(serieEntrenamientoRepository
                .existePorSesionEntrenamientoIdYSerieEjercicioRutinaId(sesionId, serieRutinaId))
                .thenReturn(true);

        assertThrows(SerieEntrenamientoAlreadyExistsException.class,
                () -> useCase.crear(sesionId, request()));

        verify(serieEntrenamientoRepository, never()).save(any());
    }

    private void stubSesion(boolean finalizada) {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionEntrenamientoRepository.obtenerPorIdYUsuarioId(sesionId, usuarioId))
                .thenReturn(Optional.of(SesionEntrenamiento.builder()
                        .id(sesionId)
                        .usuarioId(usuarioId)
                        .rutinaId(rutinaId)
                        .finalizada(finalizada)
                        .build()));
    }

    private SerieEjercicioRutina serieRutina() {
        return SerieEjercicioRutina.builder()
                .id(serieRutinaId)
                .activo(true)
                .build();
    }

    private SerieEntrenamientoRequest request() {
        return SerieEntrenamientoRequest.builder()
                .serieEjercicioRutinaId(serieRutinaId)
                .repeticionesRealizadas(10)
                .pesoUtilizado(new BigDecimal("25.50"))
                .build();
    }
}
