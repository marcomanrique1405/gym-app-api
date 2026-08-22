package com.gymtracker.gym_api.application.usecase.workout;

import com.gymtracker.gym_api.application.dto.response.workout.SesionEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoAlreadyFinishedException;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinalizarSesionEntrenamientoUseCaseTest {

    @Mock private SesionEntrenamientoRepository sesionRepository;
    @Mock private SecurityUtils securityUtils;
    @InjectMocks private FinalizarSesionEntrenamientoUseCase useCase;

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID sesionId = UUID.randomUUID();
    private final UUID rutinaId = UUID.randomUUID();

    @Test
    void finalizaSesionCorrectamente() {
        SesionEntrenamiento sesion = sesion(false);
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionRepository.obtenerPorIdYUsuarioId(sesionId, usuarioId)).thenReturn(Optional.of(sesion));
        when(sesionRepository.save(any(SesionEntrenamiento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SesionEntrenamientoResponse response = useCase.finalizarSesion(sesionId);

        assertTrue(response.isFinalizada());
        assertNotNull(response.getFechaFin());
        assertEquals(sesionId, response.getId());
        assertEquals(usuarioId, response.getUsuarioId());
        verify(sesionRepository).save(sesion);
    }

    @Test
    void rechazaCuandoLaSesionNoPerteneceAlUsuarioONoExiste() {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionRepository.obtenerPorIdYUsuarioId(sesionId, usuarioId)).thenReturn(Optional.empty());

        assertThrows(SesionEntrenamientoNotFoundException.class,
                () -> useCase.finalizarSesion(sesionId));

        verify(sesionRepository, never()).save(any());
    }

    @Test
    void rechazaCuandoLaSesionYaEstaFinalizada() {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionRepository.obtenerPorIdYUsuarioId(sesionId, usuarioId))
                .thenReturn(Optional.of(sesion(true)));

        assertThrows(SesionEntrenamientoAlreadyFinishedException.class,
                () -> useCase.finalizarSesion(sesionId));

        verify(sesionRepository, never()).save(any());
    }

    private SesionEntrenamiento sesion(boolean finalizada) {
        return SesionEntrenamiento.builder()
                .id(sesionId).usuarioId(usuarioId).rutinaId(rutinaId)
                .fechaInicio(LocalDateTime.now().minusHours(1))
                .fechaFin(finalizada ? LocalDateTime.now() : null)
                .finalizada(finalizada).build();
    }
}
