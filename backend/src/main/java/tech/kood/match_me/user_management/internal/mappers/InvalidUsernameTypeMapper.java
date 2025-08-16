package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.api.features.user.register.RegisterUserResultsDTO;
import tech.kood.match_me.user_management.internal.features.user.features.registerUser.RegisterUserResults;

@Component
public final class InvalidUsernameTypeMapper {

    public RegisterUserResultsDTO.InvalidUsernameType toInvalidUsernameType(
            RegisterUserResults.InvalidUsernameType type) {
        if (type == null) {
            throw new IllegalArgumentException("InvalidUsernameType cannot be null");
        }

        return switch (type) {
            case INVALID_CHARACTERS -> RegisterUserResultsDTO.InvalidUsernameType.INVALID_CHARACTERS;
            case TOO_SHORT -> RegisterUserResultsDTO.InvalidUsernameType.TOO_SHORT;
            case TOO_LONG -> RegisterUserResultsDTO.InvalidUsernameType.TOO_LONG;
        };
    }
}
