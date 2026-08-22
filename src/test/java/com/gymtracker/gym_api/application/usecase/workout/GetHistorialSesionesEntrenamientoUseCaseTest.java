package com.gymtracker.gym_api.application.usecase.workout;

import com.gymtracker.gym_api.application.dto.response.PageResponse;
import com.gymtracker.gym_api.application.dto.response.workout.SesionEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.PageResult;
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
        when(sesionRepository.obtenerPorUsuarioId(usuarioId, 0, 20))
                .thenReturn(new PageResult<>(List.of(reciente, anterior), 0, 20, 2, 1));

        PageResponse<SesionEntrenamientoResponse> resultado = useCase.obtenerHistorial(0, 20);

        assertEquals(2, resultado.content().size());
        assertEquals(reciente.getId(), resultado.content().get(0).getId());
        assertFalse(resultado.content().get(0).isFinalizada());
        assertEquals(anterior.getId(), resultado.content().get(1).getId());
        assertTrue(resultado.content().get(1).isFinalizada());
        assertEquals(2, resultado.totalElements());
        assertTrue(resultado.first());
        assertTrue(resultado.last());
        verify(sesionRepository).obtenerPorUsuarioId(usuarioId, 0, 20);
    }

    @Test
    void devuelveListaVaciaCuandoElUsuarioNoTieneSesiones() {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(sesionRepository.obtenerPorUsuarioId(usuarioId, 0, 20))
                .thenReturn(new PageResult<>(List.of(), 0, 20, 0, 0));

        PageResponse<SesionEntrenamientoResponse> resultado = useCase.obtenerHistorial(0, 20);

        assertNotNull(resultado);
        assertTrue(resultado.content().isEmpty());
        assertEquals(0, resultado.totalElements());
        assertTrue(resultado.first());
        assertTrue(resultado.last());
    }

    @Test
    void rechazaParametrosDePaginacionInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> useCase.obtenerHistorial(-1, 20));
        assertThrows(IllegalArgumentException.class, () -> useCase.obtenerHistorial(0, 0));
        assertThrows(IllegalArgumentException.class, () -> useCase.obtenerHistorial(0, 101));
    }

    private SesionEntrenamiento sesion(LocalDateTime inicio, boolean finalizada) {
        return SesionEntrenamiento.builder()
                .id(UUID.randomUUID()).usuarioId(usuarioId).rutinaId(UUID.randomUUID())
                .fechaInicio(inicio).fechaFin(finalizada ? inicio.plusHours(1) : null)
                .finalizada(finalizada).build();
    }
}
