package com.gymtracker.gym_api.application.usecase.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.application.dto.request.routine.serieEjercicioRutina.SerieEjercicioRutinaRequest;
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
import com.gymtracker.gym_api.shared.exception.diaRutina.DiaRutinaNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.ejercicioRutina.EjercicioRutinaNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina.SerieEjercicioRutinaAlreadyExistsException;
import com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina.SerieEjercicioRutinaInvalidRepetitionsException;
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
class CreateSerieEjercicioRutinaUseCaseTest {

    @Mock private SerieEjercicioRutinaRepository serieRepository;
    @Mock private RutinaRepository rutinaRepository;
    @Mock private DiaRutinaRepository diaRutinaRepository;
    @Mock private EjercicioRutinaRepository ejercicioRutinaRepository;
    @Mock private SecurityUtils securityUtils;
    @InjectMocks private CreateSerieEjercicioRutinaUseCase useCase;

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID rutinaId = UUID.randomUUID();
    private final UUID diaRutinaId = UUID.randomUUID();
    private final UUID ejercicioRutinaId = UUID.randomUUID();

    @Test
    void creaCorrectamenteUnaSerie() {
        stubJerarquiaValida();
        SerieEjercicioRutinaRequest request = request(1, 8, 12);
        when(serieRepository.existeActivaPorEjercicioRutinaIdYOrden(ejercicioRutinaId, 1)).thenReturn(false);
        when(serieRepository.save(any(SerieEjercicioRutina.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SerieEjercicioRutinaResponse response = useCase.crear(
                rutinaId, diaRutinaId, ejercicioRutinaId, request);

        assertEquals(ejercicioRutinaId, response.getEjercicioRutinaId());
        assertEquals(1, response.getOrden());
        assertEquals(8, response.getRepeticionesMin());
        assertEquals(12, response.getRepeticionesMax());
        verify(serieRepository).save(any(SerieEjercicioRutina.class));
    }

    @Test
    void rechazaOrdenActivoDuplicado() {
        stubJerarquiaValida();
        when(serieRepository.existeActivaPorEjercicioRutinaIdYOrden(ejercicioRutinaId, 1)).thenReturn(true);

        assertThrows(SerieEjercicioRutinaAlreadyExistsException.class,
                () -> useCase.crear(rutinaId, diaRutinaId, ejercicioRutinaId, request(1, 8, 12)));
    }

    @Test
    void rechazaCuandoRutinaNoCorrespondeAlUsuario() {
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(rutinaRepository.obtenerPorIdYUsuarioId(rutinaId, usuarioId)).thenReturn(Optional.empty());

        assertThrows(RoutineNotFoundException.class,
                () -> useCase.crear(rutinaId, diaRutinaId, ejercicioRutinaId, request(1, 8, 12)));
    }

    @Test
    void rechazaCuandoDiaNoPerteneceARutina() {
        stubRutinaValida();
        when(diaRutinaRepository.obtenerPorIdYRutinaId(diaRutinaId, rutinaId)).thenReturn(Optional.empty());

        assertThrows(DiaRutinaNotFoundException.class,
                () -> useCase.crear(rutinaId, diaRutinaId, ejercicioRutinaId, request(1, 8, 12)));
    }

    @Test
    void rechazaCuandoEjercicioRutinaNoPerteneceAlDia() {
        stubRutinaYDiaValidos();
        when(ejercicioRutinaRepository.obtenerActivoPorIdYDiaRutinaId(ejercicioRutinaId, diaRutinaId))
                .thenReturn(Optional.empty());

        assertThrows(EjercicioRutinaNotFoundException.class,
                () -> useCase.crear(rutinaId, diaRutinaId, ejercicioRutinaId, request(1, 8, 12)));
    }

    @Test
    void rechazaCuandoRepeticionesMinEsMayorQueRepeticionesMax() {
        stubJerarquiaValida();

        assertThrows(SerieEjercicioRutinaInvalidRepetitionsException.class,
                () -> useCase.crear(rutinaId, diaRutinaId, ejercicioRutinaId, request(1, 13, 12)));
    }

    private void stubJerarquiaValida() {
        stubRutinaYDiaValidos();
        EjercicioRutina ejercicio = EjercicioRutina.builder().id(ejercicioRutinaId).diaRutinaId(diaRutinaId).activo(true).build();
        when(ejercicioRutinaRepository.obtenerActivoPorIdYDiaRutinaId(ejercicioRutinaId, diaRutinaId))
                .thenReturn(Optional.of(ejercicio));
    }

    private void stubRutinaYDiaValidos() {
        stubRutinaValida();
        DiaRutina dia = DiaRutina.builder().id(diaRutinaId).rutinaId(rutinaId).build();
        when(diaRutinaRepository.obtenerPorIdYRutinaId(diaRutinaId, rutinaId)).thenReturn(Optional.of(dia));
    }

    private void stubRutinaValida() {
        Rutina rutina = Rutina.builder().id(rutinaId).usuarioId(usuarioId).activa(true).build();
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(rutinaRepository.obtenerPorIdYUsuarioId(rutinaId, usuarioId)).thenReturn(Optional.of(rutina));
    }

    private SerieEjercicioRutinaRequest request(int orden, int min, int max) {
        return SerieEjercicioRutinaRequest.builder()
                .orden(orden).repeticionesMin(min).repeticionesMax(max).build();
    }
}
