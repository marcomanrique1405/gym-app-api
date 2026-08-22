package com.gymtracker.gym_api.application.usecase.routine.serieEjercicioRutina;

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
import com.gymtracker.gym_api.shared.exception.routine.ejercicioRutina.EjercicioRutinaNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSeriesEjercicioRutinaUseCaseTest {

    @Mock private SerieEjercicioRutinaRepository serieRepository;
    @Mock private RutinaRepository rutinaRepository;
    @Mock private DiaRutinaRepository diaRutinaRepository;
    @Mock private EjercicioRutinaRepository ejercicioRutinaRepository;
    @Mock private SecurityUtils securityUtils;
    @InjectMocks private GetSeriesEjercicioRutinaUseCase useCase;

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID rutinaId = UUID.randomUUID();
    private final UUID diaRutinaId = UUID.randomUUID();
    private final UUID ejercicioRutinaId = UUID.randomUUID();

    @Test
    void obtieneCorrectamenteLasSeriesActivas() {
        stubJerarquiaValida();
        SerieEjercicioRutina serie = serie(1, 8, 12);
        when(serieRepository.obtenerActivasPorEjercicioRutinaId(ejercicioRutinaId)).thenReturn(List.of(serie));

        List<SerieEjercicioRutinaResponse> response = useCase.obtenerSeriesPorEjercicioRutina(
                rutinaId, diaRutinaId, ejercicioRutinaId);

        assertEquals(1, response.size());
        assertEquals(serie.getId(), response.get(0).getId());
        assertEquals(1, response.get(0).getOrden());
        verify(serieRepository).obtenerActivasPorEjercicioRutinaId(ejercicioRutinaId);
    }

    @Test
    void devuelveTodasLasSeriesProporcionadasPorRepositorio() {
        stubJerarquiaValida();
        List<SerieEjercicioRutina> series = List.of(serie(1, 6, 8), serie(2, 10, 12));
        when(serieRepository.obtenerActivasPorEjercicioRutinaId(ejercicioRutinaId)).thenReturn(series);

        List<SerieEjercicioRutinaResponse> response = useCase.obtenerSeriesPorEjercicioRutina(
                rutinaId, diaRutinaId, ejercicioRutinaId);

        assertEquals(series.size(), response.size());
        assertEquals(series.get(0).getId(), response.get(0).getId());
        assertEquals(series.get(1).getId(), response.get(1).getId());
    }

    @Test
    void validaJerarquiaDePertenencia() {
        stubRutinaYDiaValidos();
        when(ejercicioRutinaRepository.obtenerActivoPorIdYDiaRutinaId(ejercicioRutinaId, diaRutinaId))
                .thenReturn(Optional.empty());

        assertThrows(EjercicioRutinaNotFoundException.class,
                () -> useCase.obtenerSeriesPorEjercicioRutina(rutinaId, diaRutinaId, ejercicioRutinaId));
        verify(ejercicioRutinaRepository).obtenerActivoPorIdYDiaRutinaId(ejercicioRutinaId, diaRutinaId);
    }

    private void stubJerarquiaValida() {
        stubRutinaYDiaValidos();
        EjercicioRutina ejercicio = EjercicioRutina.builder().id(ejercicioRutinaId).diaRutinaId(diaRutinaId).activo(true).build();
        when(ejercicioRutinaRepository.obtenerActivoPorIdYDiaRutinaId(ejercicioRutinaId, diaRutinaId))
                .thenReturn(Optional.of(ejercicio));
    }

    private void stubRutinaYDiaValidos() {
        Rutina rutina = Rutina.builder().id(rutinaId).usuarioId(usuarioId).activa(true).build();
        DiaRutina dia = DiaRutina.builder().id(diaRutinaId).rutinaId(rutinaId).build();
        when(securityUtils.getCurrentUserId()).thenReturn(usuarioId);
        when(rutinaRepository.obtenerPorIdYUsuarioId(rutinaId, usuarioId)).thenReturn(Optional.of(rutina));
        when(diaRutinaRepository.obtenerPorIdYRutinaId(diaRutinaId, rutinaId)).thenReturn(Optional.of(dia));
    }

    private SerieEjercicioRutina serie(int orden, int min, int max) {
        return SerieEjercicioRutina.builder().id(UUID.randomUUID()).ejercicioRutinaId(ejercicioRutinaId)
                .orden(orden).repeticionesMin(min).repeticionesMax(max).activo(true).build();
    }
}
