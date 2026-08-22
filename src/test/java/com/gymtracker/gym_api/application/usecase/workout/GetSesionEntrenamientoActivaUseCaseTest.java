package com.gymtracker.gym_api.application.usecase.workout;

import com.gymtracker.gym_api.application.dto.response.workout.SesionEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSesionEntrenamientoActivaUseCaseTest {

    @Mock private SesionEntrenamientoRepository sesionRepository;
    @Mock private SecurityUtils securityUtils;
    @InjectMocks private GetSesionEntrenamientoActivaUseCase useCase;

    private final UUID usuarioId = UUID.randomUUID();

    @Test
    void obtieneLaSesionActivaDelUsuario() {
        UUID sesionId = UUID.randomUUID();
        UUID rutinaId = UUID.randomUUID();
        SesionEntrenamiento sesion = SesionEntrenamiento.builder()
                .id(sesionId).usuarioId(usuarioId).rutinaId(rutinaId)
                .fechaInicio(LocalDateTime.now()).finalizada(false).build();
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionRepository.obtenerNoFinalizadaPorUsuarioId(usuarioId)).thenReturn(Optional.of(sesion));

        SesionEntrenamientoResponse response = useCase.obtenerSesionActiva();

        assertEquals(sesionId, response.getId());
        assertEquals(usuarioId, response.getUsuarioId());
        assertEquals(rutinaId, response.getRutinaId());
        assertFalse(response.isFinalizada());
        assertNull(response.getFechaFin());
    }

    @Test
    void lanzaExcepcionCuandoNoHaySesionActiva() {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionRepository.obtenerNoFinalizadaPorUsuarioId(usuarioId)).thenReturn(Optional.empty());

        assertThrows(SesionEntrenamientoNotFoundException.class,
                () -> useCase.obtenerSesionActiva());
    }
}
