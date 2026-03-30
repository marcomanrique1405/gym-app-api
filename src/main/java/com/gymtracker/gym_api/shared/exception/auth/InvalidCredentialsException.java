package com.gymtracker.gym_api.shared.exception.auth;

import com.gymtracker.gym_api.shared.exception.BusinessException;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super("Credenciales inválidas");
    }
}
