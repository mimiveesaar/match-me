package tech.kood.match_me.user_management.api.DTOs.registerUser;

public record RegisterUserInvalidPasswordDTO(String password,
        RegisterUserInvalidPasswordTypeDTO type, String kind, String tracingId)
        implements RegisterUserResultsDTO {

    public RegisterUserInvalidPasswordDTO(String password, RegisterUserInvalidPasswordTypeDTO type,
            String tracingId) {
        this(password, type, "RegisterUserInvalidPassword", tracingId);
    }
}
