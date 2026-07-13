package com.gymtracker.gym_api.shared.exception.routine.ejercicioRutina;

import com.gymtracker.gym_api.shared.exception.BusinessException;

public class EjercicioRutinaAlreadyExistsException extends BusinessException {
    public EjercicioRutinaAlreadyExistsException() {
        super("Ese ejercicio ya esta asignado en el orden");
    }
}
