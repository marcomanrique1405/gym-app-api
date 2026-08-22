package com.gymtracker.gym_api.shared.exception.routine.serieEjercicioRutina;

import com.gymtracker.gym_api.shared.exception.BusinessException;

public class SerieEjercicioRutinaInvalidRepetitionsException extends BusinessException {
    public SerieEjercicioRutinaInvalidRepetitionsException() {
        super("Las repeticiones mínimas no pueden ser mayores que las máximas");
    }
}
