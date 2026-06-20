package com.gymtracker.gym_api.shared.exception.exercise;

import com.gymtracker.gym_api.shared.exception.BusinessException;

public class EjercicioAlreadyExistsException extends BusinessException {
    public EjercicioAlreadyExistsException() {
        super("El ejercicio ya existe");
    }
}
