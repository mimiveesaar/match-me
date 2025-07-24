package tech.kood.match_me.user_management.api.DTOs.registerUser;

public record RegisterUserInvalidUsernameDTO(String username,
        RegisterUserInvalidUsernameTypeDTO type, String tracingId)
        implements RegisterUserResultsDTO {
}
