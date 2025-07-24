package tech.kood.match_me.user_management.api.DTOs.registerUser;

import tech.kood.match_me.user_management.api.DTOs.UserDTO;

public record RegisterUserSuccessDTO(UserDTO user, String tracingId)
        implements RegisterUserResultsDTO {
}
