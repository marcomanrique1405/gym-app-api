package com.gymtracker.gym_api.application.usecase.workout.serieEntrenamiento;

import com.gymtracker.gym_api.application.dto.request.workout.serieEntrenamiento.SerieEntrenamientoRequest;
import com.gymtracker.gym_api.application.dto.response.workout.serieEntrenamiento.SerieEntrenamientoResponse;
import com.gymtracker.gym_api.domain.model.workout.SesionEntrenamiento;
import com.gymtracker.gym_api.domain.model.workout.serieEntrenamiento.SerieEntrenamiento;
import com.gymtracker.gym_api.domain.repository.routine.serieEjercicioRutina.SerieEjercicioRutinaRepository;
import com.gymtracker.gym_api.domain.repository.workout.SesionEntrenamientoRepository;
import com.gymtracker.gym_api.domain.repository.workout.serieEntrenamiento.SerieEntrenamientoRepository;
import com.gymtracker.gym_api.infrastructure.security.SecurityUtils;
import com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina.SerieEjercicioRutinaNotFoundException;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoAlreadyFinishedException;
import com.gymtracker.gym_api.shared.exception.workout.SesionEntrenamientoNotFoundException;
import com.gymtracker.gym_api.shared.exception.workout.serieEntrenamiento.SerieEntrenamientoAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateSerieEntrenamientoUseCase {

    private final SerieEntrenamientoRepository serieEntrenamientoRepository;
    private final SesionEntrenamientoRepository sesionEntrenamientoRepository;
    private final SerieEjercicioRutinaRepository serieEjercicioRutinaRepository;
    private final SecurityUtils securityUtils;

    public CreateSerieEntrenamientoUseCase(
            SerieEntrenamientoRepository serieEntrenamientoRepository,
            SesionEntrenamientoRepository sesionEntrenamientoRepository,
            SerieEjercicioRutinaRepository serieEjercicioRutinaRepository,
            SecurityUtils securityUtils
    ) {
        this.serieEntrenamientoRepository = serieEntrenamientoRepository;
        this.sesionEntrenamientoRepository = sesionEntrenamientoRepository;
        this.serieEjercicioRutinaRepository = serieEjercicioRutinaRepository;
        this.securityUtils = securityUtils;
    }

    public SerieEntrenamientoResponse crear(
            UUID sesionEntrenamientoId,
            SerieEntrenamientoRequest request
    ) {
        UUID usuarioId = securityUtils.getCurrentUserId();

        SesionEntrenamiento sesion = sesionEntrenamientoRepository
                .obtenerPorIdYUsuarioId(sesionEntrenamientoId, usuarioId)
                .orElseThrow(SesionEntrenamientoNotFoundException::new);

        if (sesion.isFinalizada()) {
            throw new SesionEntrenamientoAlreadyFinishedException();
        }

        serieEjercicioRutinaRepository.obtenerActivaPorId(request.getSerieEjercicioRutinaId())
                .orElseThrow(SerieEjercicioRutinaNotFoundException::new);

        if (!serieEjercicioRutinaRepository.perteneceActivaARutina(
                request.getSerieEjercicioRutinaId(), sesion.getRutinaId())) {
            throw new SerieEjercicioRutinaNotFoundException();
        }

        if (serieEntrenamientoRepository
                .existePorSesionEntrenamientoIdYSerieEjercicioRutinaId(
                        sesion.getId(),
                        request.getSerieEjercicioRutinaId()
                )) {
            throw new SerieEntrenamientoAlreadyExistsException();
        }

        SerieEntrenamiento serieEntrenamiento = SerieEntrenamiento.builder()
                .id(UUID.randomUUID())
                .sesionEntrenamientoId(sesion.getId())
                .serieEjercicioRutinaId(request.getSerieEjercicioRutinaId())
                .repeticionesRealizadas(request.getRepeticionesRealizadas())
                .pesoUtilizado(request.getPesoUtilizado())
                .build();

        return toResponse(serieEntrenamientoRepository.save(serieEntrenamiento));
    }

    private SerieEntrenamientoResponse toResponse(SerieEntrenamiento serie) {
        return SerieEntrenamientoResponse.builder()
                .id(serie.getId())
                .sesionEntrenamientoId(serie.getSesionEntrenamientoId())
                .serieEjercicioRutinaId(serie.getSerieEjercicioRutinaId())
                .repeticionesRealizadas(serie.getRepeticionesRealizadas())
                .pesoUtilizado(serie.getPesoUtilizado())
                .build();
    }
}
