package tech.kood.match_me.user_management.api.DTOs;

import java.util.List;

public class InvalidInputDTO {
    public List<FieldError> errors;
    public String kind;

    public InvalidInputDTO(List<FieldError> errors) {
        this.errors = errors;
        this.kind = "input_validation_error";
    }

    public static class FieldError {
        public String field;
        public Object rejectedValue;
        public String message;
    }
}
