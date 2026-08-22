package com.gymtracker.gym_api.shared.exception.workout;

import com.gymtracker.gym_api.shared.exception.BusinessException;

public class SesionEntrenamientoAlreadyFinishedException extends BusinessException {

    public SesionEntrenamientoAlreadyFinishedException() {
        super("La sesión de entrenamiento ya está finalizada");
    }

}
