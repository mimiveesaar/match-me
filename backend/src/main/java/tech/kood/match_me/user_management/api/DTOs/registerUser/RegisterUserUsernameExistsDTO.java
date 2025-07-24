package tech.kood.match_me.user_management.api.DTOs.registerUser;

public record RegisterUserUsernameExistsDTO(String username, String kind, String tracingId)
        implements RegisterUserResultsDTO {

    public RegisterUserUsernameExistsDTO(String username, String tracingId) {
        this(username, "RegisterUserUsernameExists", tracingId);
    }
}
