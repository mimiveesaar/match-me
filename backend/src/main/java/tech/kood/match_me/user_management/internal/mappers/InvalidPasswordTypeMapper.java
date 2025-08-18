package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.features.user.api.dto.register.RegisterUserResultsDTO;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserResults;

@Component
public final class InvalidPasswordTypeMapper {

    public RegisterUserResultsDTO.InvalidPasswordType toInvalidPasswordTypeDTO(
            RegisterUserResults.InvalidPasswordType type) {
        if (type == null) {
            throw new IllegalArgumentException("InvalidPasswordType cannot be null");
        }

        return switch (type) {
            case TOO_SHORT -> RegisterUserResultsDTO.InvalidPasswordType.TOO_SHORT;
            case TOO_LONG -> RegisterUserResultsDTO.InvalidPasswordType.TOO_LONG;
            case WEAK -> RegisterUserResultsDTO.InvalidPasswordType.WEAK;
        };
    }
}
