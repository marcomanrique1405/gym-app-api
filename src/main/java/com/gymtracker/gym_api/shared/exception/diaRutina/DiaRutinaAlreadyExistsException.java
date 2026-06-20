package com.gymtracker.gym_api.shared.exception.diaRutina;

import com.gymtracker.gym_api.shared.exception.BusinessException;

public class DiaRutinaAlreadyExistsException extends BusinessException {
    public DiaRutinaAlreadyExistsException() {
        super("El dia de rutina ya existe");
    }
}
