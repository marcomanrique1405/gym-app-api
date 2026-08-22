package com.gymtracker.gym_api.shared.validation;

import com.gymtracker.gym_api.application.dto.request.routine.ejercicioRutina.EjercicioRutinaRequest;
import com.gymtracker.gym_api.application.dto.request.workout.serieEntrenamiento.SerieEntrenamientoRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NumericPrecisionValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void pesoUtilizadoRespetaNumericDiezDos() {
        SerieEntrenamientoRequest request = SerieEntrenamientoRequest.builder()
                .serieEjercicioRutinaId(UUID.randomUUID())
                .repeticionesRealizadas(10)
                .pesoUtilizado(new BigDecimal("99999999.99"))
                .build();

        assertTrue(validator.validate(request).isEmpty());

        request.setPesoUtilizado(new BigDecimal("100000000.00"));
        assertFalse(validator.validate(request).isEmpty());

        request.setPesoUtilizado(new BigDecimal("10.001"));
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void pesosDeRutinaRespetanLaPrecisionDeFlyway() {
        EjercicioRutinaRequest request = EjercicioRutinaRequest.builder()
                .diaRutinaId(UUID.randomUUID())
                .ejercicioId(UUID.randomUUID())
                .orden(1)
                .pesoObjetivo(new BigDecimal("9999.99"))
                .incrementoPeso(new BigDecimal("99.99"))
                .sobrecargaActiva(true)
                .build();

        assertTrue(validator.validate(request).isEmpty());

        request.setPesoObjetivo(new BigDecimal("10000.00"));
        assertFalse(validator.validate(request).isEmpty());

        request.setPesoObjetivo(new BigDecimal("100.00"));
        request.setIncrementoPeso(new BigDecimal("100.00"));
        assertFalse(validator.validate(request).isEmpty());
    }
}
