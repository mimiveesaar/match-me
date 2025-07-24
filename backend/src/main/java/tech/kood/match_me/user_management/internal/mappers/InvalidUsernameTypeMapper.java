package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.api.DTOs.registerUser.RegisterUserInvalidUsernameTypeDTO;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;

@Component
public final class InvalidUsernameTypeMapper {

    public RegisterUserInvalidUsernameTypeDTO toInvalidUsernameType(
            RegisterUserResults.InvalidUsernameType type) {
        if (type == null) {
            throw new IllegalArgumentException("InvalidUsernameType cannot be null");
        }

        return switch (type) {
            case INVALID_CHARACTERS -> RegisterUserInvalidUsernameTypeDTO.INVALID_CHARACTERS;
            case TOO_SHORT -> RegisterUserInvalidUsernameTypeDTO.TOO_SHORT;
            case TOO_LONG -> RegisterUserInvalidUsernameTypeDTO.TOO_LONG;
        };
    }
}
