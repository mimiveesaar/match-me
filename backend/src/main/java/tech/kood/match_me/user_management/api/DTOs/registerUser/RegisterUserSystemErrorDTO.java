package tech.kood.match_me.user_management.api.DTOs.registerUser;

public record RegisterUserSystemErrorDTO(String message, String tracingId)
        implements RegisterUserResultsDTO {
}
