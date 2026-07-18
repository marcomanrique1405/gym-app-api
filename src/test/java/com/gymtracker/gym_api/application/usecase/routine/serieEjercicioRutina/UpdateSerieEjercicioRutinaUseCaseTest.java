package com.gymtracker.gym_api.application.usecase.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.application.dto.request.routine.serieEjercicioRutina.UpdateSerieEjercicioRutinaRequest;
import com.gymtracker.gym_api.application.dto.response.routine.serieEjercicioRutina.SerieEjercicioRutinaResponse;
import com.gymtracker.gym_api.domain.model.routine.diaRutina.DiaRutina;
import com.gymtracker.gym_api.domain.model.routine.ejercicioRutina.EjercicioRutina;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.model.routine.serieEjercicioRutina.SerieEjercicioRutina;
import com.gymtracker.gym_api.domain.repository.routine.diaRutina.DiaRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.ejercicioRutina.EjercicioRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.serieEjercicioRutina.SerieEjercicioRutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina.SerieEjercicioRutinaAlreadyExistsException;
import com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina.SerieEjercicioRutinaInvalidRepetitionsException;
import com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina.SerieEjercicioRutinaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateSerieEjercicioRutinaUseCaseTest {

    @Mock private SerieEjercicioRutinaRepository serieRepository;
    @Mock private RutinaRepository rutinaRepository;
    @Mock private DiaRutinaRepository diaRutinaRepository;
    @Mock private EjercicioRutinaRepository ejercicioRutinaRepository;
    @Mock private SecurityUtils securityUtils;
    @InjectMocks private UpdateSerieEjercicioRutinaUseCase useCase;

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
    void actualizacionCompletaCorrecta() {
        stubSerieExistente();
        when(serieRepository.existeActivaPorEjercicioRutinaIdYOrden(ejercicioRutinaId, 2)).thenReturn(false);
        stubSave();

        SerieEjercicioRutinaResponse response = actualizar(new UpdateSerieEjercicioRutinaRequest(2, 10, 15));

        assertEquals(2, response.getOrden());
        assertEquals(10, response.getRepeticionesMin());
        assertEquals(15, response.getRepeticionesMax());
        verify(serieRepository).save(any(SerieEjercicioRutina.class));
    }

    @Test
    void actualizacionParcialCorrecta() {
        stubSerieExistente();
        stubSave();

        SerieEjercicioRutinaResponse response = actualizar(new UpdateSerieEjercicioRutinaRequest(null, 10, null));

        assertEquals(1, response.getOrden());
        assertEquals(10, response.getRepeticionesMin());
        assertEquals(12, response.getRepeticionesMax());
    }

    @Test
    void rechazaOrdenDuplicado() {
        stubSerieExistente();
        when(serieRepository.existeActivaPorEjercicioRutinaIdYOrden(ejercicioRutinaId, 2)).thenReturn(true);

        assertThrows(SerieEjercicioRutinaAlreadyExistsException.class,
                () -> actualizar(new UpdateSerieEjercicioRutinaRequest(2, null, null)));
    }

    @Test
    void rechazaCuandoRepeticionesMinEsMayorQueRepeticionesMax() {
        stubSerieExistente();

        assertThrows(SerieEjercicioRutinaInvalidRepetitionsException.class,
                () -> actualizar(new UpdateSerieEjercicioRutinaRequest(null, 13, 12)));
    }

    @Test
    void validaCorrectamenteCuandoSoloCambiaRepeticionesMin() {
        stubSerieExistente();
        stubSave();

        SerieEjercicioRutinaResponse response = actualizar(new UpdateSerieEjercicioRutinaRequest(null, 11, null));

        assertEquals(11, response.getRepeticionesMin());
        assertEquals(12, response.getRepeticionesMax());
    }

    @Test
    void validaCorrectamenteCuandoSoloCambiaRepeticionesMax() {
        stubSerieExistente();
        stubSave();

        SerieEjercicioRutinaResponse response = actualizar(new UpdateSerieEjercicioRutinaRequest(null, null, 10));

        assertEquals(8, response.getRepeticionesMin());
        assertEquals(10, response.getRepeticionesMax());
    }

    @Test
    void rechazaCuandoSerieNoExiste() {
        when(serieRepository.obtenerActivaPorIdYEjercicioRutinaId(serieId, ejercicioRutinaId))
                .thenReturn(Optional.empty());

        assertThrows(SerieEjercicioRutinaNotFoundException.class,
                () -> actualizar(new UpdateSerieEjercicioRutinaRequest(2, 10, 12)));
    }

    private SerieEjercicioRutinaResponse actualizar(UpdateSerieEjercicioRutinaRequest request) {
        return useCase.update(rutinaId, diaRutinaId, ejercicioRutinaId, serieId, request);
    }

    private void stubSerieExistente() {
        SerieEjercicioRutina serie = SerieEjercicioRutina.builder().id(serieId).ejercicioRutinaId(ejercicioRutinaId)
                .orden(1).repeticionesMin(8).repeticionesMax(12).activo(true).build();
        when(serieRepository.obtenerActivaPorIdYEjercicioRutinaId(serieId, ejercicioRutinaId))
                .thenReturn(Optional.of(serie));
    }

    private void stubSave() {
        when(serieRepository.save(any(SerieEjercicioRutina.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }
}
