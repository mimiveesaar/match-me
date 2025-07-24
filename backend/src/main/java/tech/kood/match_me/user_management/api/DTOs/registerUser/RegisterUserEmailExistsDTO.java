package tech.kood.match_me.user_management.api.DTOs.registerUser;

public record RegisterUserEmailExistsDTO(String email, String kind, String tracingId)
        implements RegisterUserResultsDTO {
    public RegisterUserEmailExistsDTO(String email, String tracingId) {
        this(email, "RegisterUserEmailExists", tracingId);
    }

}
