package com.gymtracker.gym_api.application.usecase.routine.ejercicioRutina;

import com.gymtracker.gym_api.application.dto.request.routine.ejercicioRutina.EjercicioRutinaRequest;
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
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateEjercicioRutinaUseCase {

    private final EjercicioRutinaRepository ejercicioRutinaRepository;
    private final RutinaRepository rutinaRepository;
    private final DiaRutinaRepository diaRutinaRepository;
    private final EjercicioRepository ejercicioRepository;
    private final SecurityUtils securityUtils;

    public CreateEjercicioRutinaUseCase(
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

    public EjercicioRutinaResponse crear(UUID rutinaId, EjercicioRutinaRequest request) {

        UUID usuarioId = securityUtils.getCurrentUserId();

        Rutina rutina = rutinaRepository
                .obtenerPorIdYUsuarioId(rutinaId, usuarioId)
                .orElseThrow(RoutineNotFoundException::new);

        if (!rutina.getActiva()) {
            throw new RoutineNotFoundException();
        }

        DiaRutina diaRutina = diaRutinaRepository
                .obtenerPorIdYRutinaId(request.getDiaRutinaId(), rutina.getId())
                .orElseThrow(DiaRutinaNotFoundException::new);

        Ejercicio ejercicio = ejercicioRepository
                .obtenerPorId(request.getEjercicioId())
                .orElseThrow(EjercicioNotFoundException::new);

        if (!ejercicio.getActivo()) throw new EjercicioNotFoundException();

        if (ejercicioRutinaRepository.existeActivoPorDiaRutinaIdYEjercicioId(
                diaRutina.getId(), ejercicio.getId())) {
            throw new EjercicioRutinaAlreadyExistsException();
        }

        boolean existeOrden = ejercicioRutinaRepository
                .existeActivoPorDiaRutinaIdYOrden(diaRutina.getId(), request.getOrden());

        if (existeOrden) {
            throw new EjercicioRutinaAlreadyExistsException();
        }

        EjercicioRutina ejercicioRutina = EjercicioRutina.builder()
                .id(UUID.randomUUID())
                .diaRutinaId(diaRutina.getId())
                .ejercicioId(ejercicio.getId())
                .orden(request.getOrden())
                .pesoObjetivo(request.getPesoObjetivo())
                .incrementoPeso(request.getIncrementoPeso())
                .sobrecargaActiva(request.getSobrecargaActiva())
                .activo(true)
                .build();

        EjercicioRutina ejercicioRutinaGuardado = ejercicioRutinaRepository.save(ejercicioRutina);

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
