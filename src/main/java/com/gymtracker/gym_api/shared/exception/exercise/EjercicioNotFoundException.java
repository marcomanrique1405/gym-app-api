package com.gymtracker.gym_api.shared.exception.exercise;

import com.gymtracker.gym_api.shared.exception.NotFoundException;

public class EjercicioNotFoundException extends NotFoundException {
    public EjercicioNotFoundException() {
        super("Ejercicio no encontrado");
    }
}
