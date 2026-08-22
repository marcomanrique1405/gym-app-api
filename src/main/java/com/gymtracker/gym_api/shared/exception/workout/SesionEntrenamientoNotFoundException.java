package com.gymtracker.gym_api.shared.exception.workout;

import com.gymtracker.gym_api.shared.exception.NotFoundException;

public class SesionEntrenamientoNotFoundException extends NotFoundException {

    public SesionEntrenamientoNotFoundException() {
        super("Sesión de entrenamiento no encontrada");
    }

}
