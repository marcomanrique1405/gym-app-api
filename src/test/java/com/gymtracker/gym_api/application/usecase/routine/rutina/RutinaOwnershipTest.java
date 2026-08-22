package com.gymtracker.gym_api.application.usecase.routine.rutina;

import com.gymtracker.gym_api.application.dto.request.routine.rutina.UpdateRutinaRequest;
import com.gymtracker.gym_api.domain.enums.TipoProgresion;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RutinaOwnershipTest {
    private RutinaRepository repository;
    private SecurityUtils securityUtils;
    private UUID rutinaId;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        repository = mock(RutinaRepository.class);
        securityUtils = mock(SecurityUtils.class);
        rutinaId = UUID.randomUUID();
        usuarioId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
    }

    @Test
    void propietarioPuedeConsultarSuRutina() {
        when(repository.obtenerPorIdYUsuarioId(rutinaId, usuarioId))
                .thenReturn(Optional.of(rutina(usuarioId)));
        var response = new GetRutinaByIdUseCase(repository, securityUtils).obtenerRutina(rutinaId);
        assertEquals(rutinaId, response.getId());
    }

    @Test
    void usuarioAjenoNoPuedeConsultarRutina() {
        when(repository.obtenerPorIdYUsuarioId(rutinaId, usuarioId)).thenReturn(Optional.empty());
        assertThrows(RoutineNotFoundException.class,
                () -> new GetRutinaByIdUseCase(repository, securityUtils).obtenerRutina(rutinaId));
    }

    @Test
    void usuarioAjenoNoPuedeActualizarRutina() {
        when(repository.obtenerPorIdYUsuarioId(rutinaId, usuarioId)).thenReturn(Optional.empty());
        assertThrows(RoutineNotFoundException.class,
                () -> new UpdateRutinaUseCase(repository, securityUtils)
                        .actualizarRutina(rutinaId, new UpdateRutinaRequest()));
        verify(repository, never()).save(any());
    }

    @Test
    void usuarioAjenoNoPuedeEliminarRutina() {
        when(repository.obtenerPorIdYUsuarioId(rutinaId, usuarioId)).thenReturn(Optional.empty());
        assertThrows(RoutineNotFoundException.class,
                () -> new DeleteRutinaUseCase(repository, securityUtils).eliminarRutina(rutinaId));
        verify(repository, never()).save(any());
    }

    private Rutina rutina(UUID owner) {
        return Rutina.builder().id(rutinaId).usuarioId(owner).nombre("Fuerza")
                .tipoProgresion(TipoProgresion.MANUAL).activa(true)
                .fechaCreacion(LocalDateTime.now()).build();
    }
}
