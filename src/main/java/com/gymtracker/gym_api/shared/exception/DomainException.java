package com.gymtracker.gym_api.shared.exception;

public class DomainException extends RuntimeException {
    protected DomainException(String massage) {
        super(massage);
    }
}
