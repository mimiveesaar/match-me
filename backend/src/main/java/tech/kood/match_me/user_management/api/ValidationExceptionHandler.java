package tech.kood.match_me.user_management.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tech.kood.match_me.user_management.api.DTOs.InvalidInputDTO;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InvalidInputDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<InvalidInputDTO.FieldError> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            InvalidInputDTO.FieldError fieldError =
                    new InvalidInputDTO.FieldError();
            fieldError.field = error.getField();
            fieldError.rejectedValue = error.getRejectedValue();
            fieldError.message = error.getDefaultMessage();
            errors.add(fieldError);
        });

        return new ResponseEntity<>(new InvalidInputDTO(errors), HttpStatus.BAD_REQUEST);
    }
}
