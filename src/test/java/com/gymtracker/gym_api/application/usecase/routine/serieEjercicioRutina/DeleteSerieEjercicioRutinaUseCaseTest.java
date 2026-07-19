package com.gymtracker.gym_api.application.usecase.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.domain.model.routine.diaRutina.DiaRutina;
import com.gymtracker.gym_api.domain.model.routine.ejercicioRutina.EjercicioRutina;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.model.routine.serieEjercicioRutina.SerieEjercicioRutina;
import com.gymtracker.gym_api.domain.repository.routine.diaRutina.DiaRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.ejercicioRutina.EjercicioRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.serieEjercicioRutina.SerieEjercicioRutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina.SerieEjercicioRutinaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteSerieEjercicioRutinaUseCaseTest {

    @Mock private SerieEjercicioRutinaRepository serieRepository;
    @Mock private RutinaRepository rutinaRepository;
    @Mock private DiaRutinaRepository diaRutinaRepository;
    @Mock private EjercicioRutinaRepository ejercicioRutinaRepository;
    @Mock private SecurityUtils securityUtils;
    @InjectMocks private DeleteSerieEjercicioRutinaUseCase useCase;

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID rutinaId = UUID.randomUUID();
    private final UUID diaRutinaId = UUID.randomUUID();
    private final UUID ejercicioRutinaId = UUID.randomUUID();
    private final UUID serieId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        Rutina rutina = Rutina.builder().id(rutinaId).usuarioId(usuarioId).activa(true).build();
        DiaRutina dia = DiaRutina.builder().id(diaRutinaId).rutinaId(rutinaId).build();
        EjercicioRutina ejercicio = EjercicioRutina.builder().id(ejercicioRutinaId).diaRutinaId(diaRutinaId).activo(true).build();
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(rutinaRepository.obtenerPorIdYUsuarioId(rutinaId, usuarioId)).thenReturn(Optional.of(rutina));
        when(diaRutinaRepository.obtenerPorIdYRutinaId(diaRutinaId, rutinaId)).thenReturn(Optional.of(dia));
        when(ejercicioRutinaRepository.obtenerActivoPorIdYDiaRutinaId(ejercicioRutinaId, diaRutinaId))
                .thenReturn(Optional.of(ejercicio));
    }

    @Test
    void realizaCorrectamenteBorradoLogico() {
        stubSerieExistente();

        eliminar();

        ArgumentCaptor<SerieEjercicioRutina> captor = ArgumentCaptor.forClass(SerieEjercicioRutina.class);
        verify(serieRepository).save(captor.capture());
        assertEquals(serieId, captor.getValue().getId());
        assertFalse(captor.getValue().isActivo());
    }

    @Test
    void activoCambiaAFalse() {
        stubSerieExistente();

        eliminar();

        ArgumentCaptor<SerieEjercicioRutina> captor = ArgumentCaptor.forClass(SerieEjercicioRutina.class);
        verify(serieRepository).save(captor.capture());
        assertFalse(captor.getValue().isActivo());
    }

    @Test
    void guardaLaSerieModificada() {
        stubSerieExistente();

        eliminar();

        verify(serieRepository).save(org.mockito.ArgumentMatchers.any(SerieEjercicioRutina.class));
    }

    @Test
    void rechazaCuandoSerieNoExiste() {
        when(serieRepository.obtenerActivaPorIdYEjercicioRutinaId(serieId, ejercicioRutinaId))
                .thenReturn(Optional.empty());

        assertThrows(SerieEjercicioRutinaNotFoundException.class, this::eliminar);
    }

    private void eliminar() {
        useCase.eliminar(rutinaId, diaRutinaId, ejercicioRutinaId, serieId);
    }

    private void stubSerieExistente() {
        SerieEjercicioRutina serie = SerieEjercicioRutina.builder().id(serieId).ejercicioRutinaId(ejercicioRutinaId)
                .orden(1).repeticionesMin(8).repeticionesMax(12).activo(true).build();
        when(serieRepository.obtenerActivaPorIdYEjercicioRutinaId(serieId, ejercicioRutinaId))
                .thenReturn(Optional.of(serie));
    }
}
