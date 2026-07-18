package com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.shared.exception.NotFoundException;

public class SerieEjercicioRutinaNotFoundException extends NotFoundException {
    public SerieEjercicioRutinaNotFoundException() {
        super("No se encontró la serie del ejercicio de la rutina");
    }
}
