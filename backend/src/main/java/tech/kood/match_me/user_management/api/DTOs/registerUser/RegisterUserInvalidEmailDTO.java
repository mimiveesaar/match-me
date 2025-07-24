package tech.kood.match_me.user_management.api.DTOs.registerUser;

public record RegisterUserInvalidEmailDTO(String email, String kind, String tracingId)
        implements RegisterUserResultsDTO {
    public RegisterUserInvalidEmailDTO(String email, String tracingId) {
        this(email, "RegisterUserInvalidEmail", tracingId);
    }
}
