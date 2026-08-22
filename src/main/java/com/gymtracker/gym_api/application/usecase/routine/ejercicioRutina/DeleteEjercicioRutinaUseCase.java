package com.gymtracker.gym_api.application.usecase.routine.ejercicioRutina;

import com.gymtracker.gym_api.domain.model.routine.diaRutina.DiaRutina;
import com.gymtracker.gym_api.domain.model.routine.ejercicioRutina.EjercicioRutina;
import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.diaRutina.DiaRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.ejercicioRutina.EjercicioRutinaRepository;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.diaRutina.DiaRutinaNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.RoutineNotFoundException;
import com.gymtracker.gym_api.shared.exception.routine.ejercicioRutina.EjercicioRutinaNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteEjercicioRutinaUseCase {
    private final EjercicioRutinaRepository ejercicioRutinaRepository;
    private final RutinaRepository rutinaRepository;
    private final DiaRutinaRepository diaRutinaRepository;
    private final SecurityUtils securityUtils;

    public DeleteEjercicioRutinaUseCase(
            EjercicioRutinaRepository ejercicioRutinaRepository,
            RutinaRepository rutinaRepository,
            DiaRutinaRepository diaRutinaRepository,
            SecurityUtils securityUtils
    ) {
        this.ejercicioRutinaRepository = ejercicioRutinaRepository;
        this.rutinaRepository = rutinaRepository;
        this.diaRutinaRepository = diaRutinaRepository;
        this.securityUtils = securityUtils;
    }

    @Transactional
    public void eliminar(
            UUID rutinaId,
            UUID diaRutinaId,
            UUID ejercicioRutinaId
    ) {
        UUID usuarioId = securityUtils.getCurrentUserId();

        Rutina rutina = rutinaRepository
                .obtenerPorIdYUsuarioId(rutinaId, usuarioId)
                .orElseThrow(RoutineNotFoundException::new);

        if (Boolean.FALSE.equals(rutina.getActiva())) {
            throw new RoutineNotFoundException();
        }

        DiaRutina diaRutina = diaRutinaRepository
                .obtenerPorIdYRutinaId(diaRutinaId, rutina.getId())
                .orElseThrow(DiaRutinaNotFoundException::new);

        EjercicioRutina ejercicioRutina = ejercicioRutinaRepository
                .obtenerActivoPorIdYDiaRutinaId(ejercicioRutinaId, diaRutina.getId())
                .orElseThrow(EjercicioRutinaNotFoundException::new);

        EjercicioRutina ejercicioRutinaDesactivado = EjercicioRutina.builder()
                .id(ejercicioRutina.getId())
                .diaRutinaId(ejercicioRutina.getDiaRutinaId())
                .ejercicioId(ejercicioRutina.getEjercicioId())
                .orden(ejercicioRutina.getOrden())
                .pesoObjetivo(ejercicioRutina.getPesoObjetivo())
                .incrementoPeso(ejercicioRutina.getIncrementoPeso())
                .sobrecargaActiva(ejercicioRutina.isSobrecargaActiva())
                .activo(false)
                .build();

        ejercicioRutinaRepository.save(ejercicioRutinaDesactivado);
    }
}
