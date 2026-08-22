package com.gymtracker.gym_api.shared.exception.workout.serieEntrenamiento;

import com.gymtracker.gym_api.shared.exception.BusinessException;

public class SerieEntrenamientoAlreadyExistsException extends BusinessException {

    public SerieEntrenamientoAlreadyExistsException() {
        super("La serie de rutina ya está registrada en esta sesión");
    }
}
