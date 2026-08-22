package com.gymtracker.gym_api.application.usecase.workout;

import com.gymtracker.gym_api.application.dto.response.workout.SesionEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoAlreadyActiveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IniciarSesionEntrenamientoUseCaseTest {

    @Mock private SesionEntrenamientoRepository sesionRepository;
    @Mock private RutinaRepository rutinaRepository;
    @Mock private SecurityUtils securityUtils;
    @InjectMocks private IniciarSesionEntrenamientoUseCase useCase;

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID rutinaId = UUID.randomUUID();

    @Test
    void iniciaSesionCorrectamente() {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(rutinaRepository.obtenerPorIdYUsuarioId(rutinaId, usuarioId))
                .thenReturn(Optional.of(rutinaActiva()));
        when(sesionRepository.obtenerNoFinalizadaPorUsuarioId(usuarioId)).thenReturn(Optional.empty());
        when(sesionRepository.save(any(SesionEntrenamiento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SesionEntrenamientoResponse response = useCase.iniciarSesion(rutinaId);

        assertNotNull(response.getId());
        assertEquals(usuarioId, response.getUsuarioId());
        assertEquals(rutinaId, response.getRutinaId());
        assertNotNull(response.getFechaInicio());
        assertNull(response.getFechaFin());
        assertFalse(response.isFinalizada());

        ArgumentCaptor<SesionEntrenamiento> captor = ArgumentCaptor.forClass(SesionEntrenamiento.class);
        verify(sesionRepository).save(captor.capture());
        assertEquals(usuarioId, captor.getValue().getUsuarioId());
        assertEquals(rutinaId, captor.getValue().getRutinaId());
    }

    @Test
    void rechazaCuandoLaRutinaNoPerteneceAlUsuario() {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(rutinaRepository.obtenerPorIdYUsuarioId(rutinaId, usuarioId)).thenReturn(Optional.empty());

        assertThrows(RoutineNotFoundException.class, () -> useCase.iniciarSesion(rutinaId));

        verifyNoInteractions(sesionRepository);
    }

    @Test
    void rechazaCuandoLaRutinaEstaInactiva() {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(rutinaRepository.obtenerPorIdYUsuarioId(rutinaId, usuarioId))
                .thenReturn(Optional.of(Rutina.builder().id(rutinaId).usuarioId(usuarioId).activa(false).build()));

        assertThrows(RoutineNotFoundException.class, () -> useCase.iniciarSesion(rutinaId));

        verifyNoInteractions(sesionRepository);
    }

    @Test
    void rechazaCuandoYaExisteUnaSesionActiva() {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(rutinaRepository.obtenerPorIdYUsuarioId(rutinaId, usuarioId))
                .thenReturn(Optional.of(rutinaActiva()));
        when(sesionRepository.obtenerNoFinalizadaPorUsuarioId(usuarioId))
                .thenReturn(Optional.of(SesionEntrenamiento.builder().finalizada(false).build()));

        assertThrows(SesionEntrenamientoAlreadyActiveException.class,
                () -> useCase.iniciarSesion(rutinaId));

        verify(sesionRepository, never()).save(any());
    }

    private Rutina rutinaActiva() {
        return Rutina.builder().id(rutinaId).usuarioId(usuarioId).activa(true).build();
    }
}
