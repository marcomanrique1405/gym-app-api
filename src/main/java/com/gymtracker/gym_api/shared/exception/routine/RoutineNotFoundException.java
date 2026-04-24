package com.gymtracker.gym_api.shared.exception.routine;

import com.gymtracker.gym_api.shared.exception.NotFoundException;

public class RoutineNotFoundException extends NotFoundException {
    public RoutineNotFoundException() {
        super("Rutina no encontrada");
    }
}
