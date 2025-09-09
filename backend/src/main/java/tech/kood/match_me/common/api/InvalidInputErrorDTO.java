package tech.kood.match_me.common.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintViolation;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record InvalidInputErrorDTO(@JsonProperty("errors") List<InputFieldErrorDTO> errors) {

    private static InvalidInputErrorDTO from(Set<ConstraintViolation<?>> violations) {
        List<InputFieldErrorDTO> fieldErrors = violations.stream()
                .map(violation -> new InputFieldErrorDTO(
                        violation.getPropertyPath().toString(),
                        violation.getInvalidValue(),
                        violation.getMessage()))
                .toList();
        return new InvalidInputErrorDTO(fieldErrors);
    }

    public static <T> InvalidInputErrorDTO fromValidation(Set<ConstraintViolation<T>> violations) {
        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>(violations);
        return from(constraintViolations);
    }

    public static InvalidInputErrorDTO fromException(CheckedConstraintViolationException exception) {
        return from(exception.getConstraintViolations());
    }
}
