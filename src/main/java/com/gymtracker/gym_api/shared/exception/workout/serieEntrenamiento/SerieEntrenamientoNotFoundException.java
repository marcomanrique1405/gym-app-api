package com.gymtracker.gym_api.shared.exception.workout.serieEntrenamiento;

import com.gymtracker.gym_api.shared.exception.NotFoundException;

public class SerieEntrenamientoNotFoundException extends NotFoundException {

    public SerieEntrenamientoNotFoundException() {
        super("Serie de entrenamiento no encontrada en la sesión indicada");
    }
}
