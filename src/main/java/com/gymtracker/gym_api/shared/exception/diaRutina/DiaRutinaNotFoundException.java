package com.gymtracker.gym_api.shared.exception.diaRutina;

import com.gymtracker.gym_api.shared.exception.NotFoundException;

public class DiaRutinaNotFoundException extends NotFoundException {
    public DiaRutinaNotFoundException() {
        super("El dia rutina no existe.");
    }
}
