package com.gymtracker.gym_api.application.usecase.workout;

import com.gymtracker.gym_api.application.dto.response.workout.SesionEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetHistorialSesionesEntrenamientoUseCaseTest {

    @Mock private SesionEntrenamientoRepository sesionRepository;
    @Mock private SecurityUtils securityUtils;
    @InjectMocks private GetHistorialSesionesEntrenamientoUseCase useCase;

    private final UUID usuarioId = UUID.randomUUID();

    @Test
    void obtieneYMapeaElHistorialDelUsuario() {
        SesionEntrenamiento reciente = sesion(LocalDateTime.now(), false);
        SesionEntrenamiento anterior = sesion(LocalDateTime.now().minusDays(1), true);
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionRepository.obtenerPorUsuarioId(usuarioId)).thenReturn(List.of(reciente, anterior));

        List<SesionEntrenamientoResponse> resultado = useCase.obtenerHistorial();

        assertEquals(2, resultado.size());
        assertEquals(reciente.getId(), resultado.get(0).getId());
        assertFalse(resultado.get(0).isFinalizada());
        assertEquals(anterior.getId(), resultado.get(1).getId());
        assertTrue(resultado.get(1).isFinalizada());
        verify(sesionRepository).obtenerPorUsuarioId(usuarioId);
    }

    @Test
    void devuelveListaVaciaCuandoElUsuarioNoTieneSesiones() {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionRepository.obtenerPorUsuarioId(usuarioId)).thenReturn(List.of());

        List<SesionEntrenamientoResponse> resultado = useCase.obtenerHistorial();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    private SesionEntrenamiento sesion(LocalDateTime inicio, boolean finalizada) {
        return SesionEntrenamiento.builder()
                .id(UUID.randomUUID()).usuarioId(usuarioId).rutinaId(UUID.randomUUID())
                .fechaInicio(inicio).fechaFin(finalizada ? inicio.plusHours(1) : null)
                .finalizada(finalizada).build();
    }
}
