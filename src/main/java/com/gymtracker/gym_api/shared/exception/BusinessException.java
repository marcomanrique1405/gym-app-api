package com.gymtracker.gym_api.shared.exception;

public abstract class BusinessException extends DomainException {
    protected BusinessException(String massage) {
        super(massage);
    }
}
