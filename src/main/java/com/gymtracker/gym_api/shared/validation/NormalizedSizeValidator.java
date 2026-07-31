package com.gymtracker.gym_api.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NormalizedSizeValidator implements ConstraintValidator<NormalizedSize, String> {
    private int min;
    private int max;

    @Override
    public void initialize(NormalizedSize constraint) {
        min = constraint.min();
        max = constraint.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        int normalizedLength = value.trim().length();
        return normalizedLength >= min && normalizedLength <= max;
    }
}
