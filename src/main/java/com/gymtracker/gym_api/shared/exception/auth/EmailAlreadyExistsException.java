package com.gymtracker.gym_api.shared.exception.auth;

import com.gymtracker.gym_api.shared.exception.BusinessException;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException() {
        super("El email ya está registrado");
    }
}
