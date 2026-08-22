package com.gymtracker.gym_api.shared.exception.routine.ejercicioRutina;

import com.gymtracker.gym_api.shared.exception.NotFoundException;

public class EjercicioRutinaNotFoundException extends NotFoundException {
    public EjercicioRutinaNotFoundException() {
        super("No se encontro el ejercicio de la rutina ");
    }
}
