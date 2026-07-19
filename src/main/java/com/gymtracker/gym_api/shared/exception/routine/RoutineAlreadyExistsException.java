package com.gymtracker.gym_api.shared.exception.routine;

import com.gymtracker.gym_api.shared.exception.BusinessException;

public class RoutineAlreadyExistsException extends BusinessException {
    public RoutineAlreadyExistsException() {
        super("Esta rutina ya existe");
    }
}
