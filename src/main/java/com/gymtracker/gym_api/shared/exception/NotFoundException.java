package com.gymtracker.gym_api.shared.exception;

public abstract class NotFoundException extends DomainException {
    protected NotFoundException(String massage) {
        super(massage);
    }
}
