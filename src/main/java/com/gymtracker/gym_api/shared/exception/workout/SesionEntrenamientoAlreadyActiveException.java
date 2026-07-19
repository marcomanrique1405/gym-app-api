package com.gymtracker.gym_api.shared.exception.workout;

import com.gymtracker.gym_api.shared.exception.BusinessException;

public class SesionEntrenamientoAlreadyActiveException extends BusinessException {

    public SesionEntrenamientoAlreadyActiveException() {
        super("El usuario ya tiene una sesión de entrenamiento activa");
    }

}
