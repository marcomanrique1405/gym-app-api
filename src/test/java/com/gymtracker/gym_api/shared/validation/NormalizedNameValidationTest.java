package com.gymtracker.gym_api.shared.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymtracker.gym_api.application.dto.request.auth.RegisterUserRequest;
import com.gymtracker.gym_api.application.dto.request.exercise.EjercicioRequest;
import com.gymtracker.gym_api.application.dto.request.exercise.UpdateEjercicioRequest;
import com.gymtracker.gym_api.application.dto.request.routine.rutina.CreateRutinaRequest;
import com.gymtracker.gym_api.application.dto.request.routine.rutina.UpdateRutinaRequest;
import com.gymtracker.gym_api.domain.enums.GrupoMuscular;
import com.gymtracker.gym_api.domain.enums.TipoProgresion;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NormalizedNameValidationTest {
    private static jakarta.validation.ValidatorFactory validatorFactory;
    private static Validator validator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void registroRechazaNombreQueQuedaConUnCaracterTrasTrim() throws Exception {
        RegisterUserRequest request = objectMapper.readValue("""
                {"nombre":" a ","email":"user@example.com","password":"password123"}
                """, RegisterUserRequest.class);

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rutinasRechazanNombresInvalidosTrasTrim() throws Exception {
        CreateRutinaRequest create = new CreateRutinaRequest(" a ", TipoProgresion.MANUAL);
        UpdateRutinaRequest update = objectMapper.readValue("{\"nombre\":\"   \"}", UpdateRutinaRequest.class);

        assertFalse(validator.validate(create).isEmpty());
        assertFalse(validator.validate(update).isEmpty());
    }

    @Test
    void ejerciciosRechazanNombresInvalidosTrasTrim() {
        EjercicioRequest create = new EjercicioRequest(" a ", GrupoMuscular.PECHO, "Descripción");
        UpdateEjercicioRequest update = new UpdateEjercicioRequest("   ", null, null);

        assertFalse(validator.validate(create).isEmpty());
        assertFalse(validator.validate(update).isEmpty());
    }

    @Test
    void patchPermiteNombreAusenteYNombreNormalizadoValido() throws Exception {
        UpdateRutinaRequest withoutName = objectMapper.readValue("{}", UpdateRutinaRequest.class);
        UpdateEjercicioRequest validName = new UpdateEjercicioRequest(" ab ", null, null);

        assertTrue(validator.validate(withoutName).isEmpty());
        assertTrue(validator.validate(validName).isEmpty());
    }
}
