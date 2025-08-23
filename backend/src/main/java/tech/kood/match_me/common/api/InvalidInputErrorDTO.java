package tech.kood.match_me.common.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintViolation;

import java.util.List;
import java.util.Set;

public record InvalidInputErrorDTO(@JsonProperty("errors") List<InputFieldErrorDTO> errors) {

    public static <T> InvalidInputErrorDTO from(Set<ConstraintViolation<T>> violations) {
        List<InputFieldErrorDTO> fieldErrors = violations.stream()
                .map(violation -> new InputFieldErrorDTO(
                        violation.getPropertyPath().toString(),
                        violation.getInvalidValue(),
                        violation.getMessage()))
                .toList();
        return new InvalidInputErrorDTO(fieldErrors);
    }
}
