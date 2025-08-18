package tech.kood.match_me.user_management.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InvalidInputErrorDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<InvalidInputErrorDTO.FieldError> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            InvalidInputErrorDTO.FieldError fieldError =
                    new InvalidInputErrorDTO.FieldError();
            fieldError.field = error.getField();
            fieldError.rejectedValue = error.getRejectedValue();
            fieldError.message = error.getDefaultMessage();
            errors.add(fieldError);
        });

        return new ResponseEntity<>(new InvalidInputErrorDTO(errors), HttpStatus.BAD_REQUEST);
    }
}
