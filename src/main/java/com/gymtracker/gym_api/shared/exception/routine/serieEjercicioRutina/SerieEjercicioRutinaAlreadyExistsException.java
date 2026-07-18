package com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.shared.exception.BusinessException;

public class SerieEjercicioRutinaAlreadyExistsException extends BusinessException {
    public SerieEjercicioRutinaAlreadyExistsException() {
        super("Ya existe una serie activa con ese orden");
    }
}
