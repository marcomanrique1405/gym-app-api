package com.gymtracker.gym_api.application.usecase.routine.ejercicioRutina;

import com.gymtracker.gym_api.application.dto.request.routine.ejercicioRutina.UpdateEjercicioRutinaRequest;
import com.gymtracker.gym_api.application.dto.response.routine.ejercicioRutina.EjercicioRutinaResponse;
import com.gymtracker.gym_api.domain.model.exercise.Ejercicio;
import com.gymtracker.gym_api.domain.model.routine.diaRutina.DiaRutina;
import com.gymtracker.gym_api.domain.model.routine.ejercicioRutina.EjercicioRutina;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.repository.exercise.EjercicioRepository;
import com.gymtracker.gym_api.domain.repository.routine.diaRutina.DiaRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.ejercicioRutina.EjercicioRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.diaRutina.DiaRutinaNotFoundException;
import com.gymtracker.gym_api.shared.exception.exercise.EjercicioNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.ejercicioRutina.EjercicioRutinaAlreadyExistsException;
import com.gymtracker.gym_api.shared.exception.routine.ejercicioRutina.EjercicioRutinaNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UpdateEjercicioRutinaUseCase {

    private final EjercicioRutinaRepository ejercicioRutinaRepository;
    private final RutinaRepository rutinaRepository;
    private final DiaRutinaRepository diaRutinaRepository;
    private final EjercicioRepository ejercicioRepository;
    private final SecurityUtils securityUtils;

    public UpdateEjercicioRutinaUseCase(
            EjercicioRutinaRepository ejercicioRutinaRepository,
            RutinaRepository rutinaRepository,
            DiaRutinaRepository diaRutinaRepository,
            EjercicioRepository ejercicioRepository,
            SecurityUtils securityUtils
    ) {
        this.ejercicioRutinaRepository = ejercicioRutinaRepository;
        this.rutinaRepository = rutinaRepository;
        this.diaRutinaRepository = diaRutinaRepository;
        this.ejercicioRepository = ejercicioRepository;
        this.securityUtils = securityUtils;
    }

    public EjercicioRutinaResponse update(
            UUID rutinaId,
            UUID diaRutinaId,
            UUID ejercicioRutinaId,
            UpdateEjercicioRutinaRequest request
    ) {
        UUID usuarioId = securityUtils.getCurrentUserId();

        Rutina rutina = rutinaRepository
                .obtenerPorIdYUsuarioId(rutinaId, usuarioId)
                .orElseThrow(RoutineNotFoundException::new);

        if (!rutina.getActiva()) {
            throw new RoutineNotFoundException();
        }

        DiaRutina diaRutinaActual = diaRutinaRepository
                .obtenerPorIdYRutinaId(diaRutinaId, rutina.getId())
                .orElseThrow(DiaRutinaNotFoundException::new);

        EjercicioRutina ejercicioRutinaActual = ejercicioRutinaRepository
                .obtenerActivoPorIdYDiaRutinaId(ejercicioRutinaId, diaRutinaActual.getId())
                .orElseThrow(EjercicioRutinaNotFoundException::new);

        UUID diaRutinaIdFinal = request.getDiaRutinaId() != null
                ? request.getDiaRutinaId()
                : ejercicioRutinaActual.getDiaRutinaId();

        if (!diaRutinaIdFinal.equals(ejercicioRutinaActual.getDiaRutinaId())) {
            diaRutinaRepository
                    .obtenerPorIdYRutinaId(diaRutinaIdFinal, rutina.getId())
                    .orElseThrow(DiaRutinaNotFoundException::new);
        }

        UUID ejercicioIdFinal = request.getEjercicioId() != null
                ? request.getEjercicioId()
                : ejercicioRutinaActual.getEjercicioId();

        if (!ejercicioIdFinal.equals(ejercicioRutinaActual.getEjercicioId())) {
            Ejercicio ejercicio = ejercicioRepository
                    .obtenerPorId(ejercicioIdFinal)
                    .orElseThrow(EjercicioNotFoundException::new);

            ejercicioIdFinal = ejercicio.getId();
        }

        Integer ordenFinal = request.getOrden() != null
                ? request.getOrden()
                : ejercicioRutinaActual.getOrden();

        BigDecimal pesoObjetivoFinal = request.getPesoObjetivo() != null
                ? request.getPesoObjetivo()
                : ejercicioRutinaActual.getPesoObjetivo();

        BigDecimal incrementoPesoFinal = request.getIncrementoPeso() != null
                ? request.getIncrementoPeso()
                : ejercicioRutinaActual.getIncrementoPeso();

        Boolean sobrecargaActivaFinal = request.getSobrecargaActiva() != null
                ? request.getSobrecargaActiva()
                : ejercicioRutinaActual.isSobrecargaActiva();

        boolean cambioDia = !diaRutinaIdFinal.equals(ejercicioRutinaActual.getDiaRutinaId());
        boolean cambioOrden = !ordenFinal.equals(ejercicioRutinaActual.getOrden());

        if (cambioDia || cambioOrden) {
            boolean existeOrden = ejercicioRutinaRepository
                    .existeActivoPorDiaRutinaIdYOrden(diaRutinaIdFinal, ordenFinal);

            if (existeOrden) {
                throw new EjercicioRutinaAlreadyExistsException();
            }
        }

        EjercicioRutina ejercicioRutinaActualizado = EjercicioRutina.builder()
                .id(ejercicioRutinaActual.getId())
                .diaRutinaId(diaRutinaIdFinal)
                .ejercicioId(ejercicioIdFinal)
                .orden(ordenFinal)
                .pesoObjetivo(pesoObjetivoFinal)
                .incrementoPeso(incrementoPesoFinal)
                .sobrecargaActiva(sobrecargaActivaFinal)
                .activo(ejercicioRutinaActual.isActivo())
                .build();

        EjercicioRutina ejercicioRutinaGuardado = ejercicioRutinaRepository.save(ejercicioRutinaActualizado);

        return EjercicioRutinaResponse.builder()
                .id(ejercicioRutinaGuardado.getId())
                .diaRutinaId(ejercicioRutinaGuardado.getDiaRutinaId())
                .ejercicioId(ejercicioRutinaGuardado.getEjercicioId())
                .orden(ejercicioRutinaGuardado.getOrden())
                .pesoObjetivo(ejercicioRutinaGuardado.getPesoObjetivo())
                .incrementoPeso(ejercicioRutinaGuardado.getIncrementoPeso())
                .sobrecargaActiva(ejercicioRutinaGuardado.isSobrecargaActiva())
                .activo(ejercicioRutinaGuardado.isActivo())
                .build();
    }
}