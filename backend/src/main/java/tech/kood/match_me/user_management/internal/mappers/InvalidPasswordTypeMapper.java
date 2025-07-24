package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.api.DTOs.registerUser.RegisterUserInvalidPasswordTypeDTO;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;

@Component
public final class InvalidPasswordTypeMapper {

    public RegisterUserInvalidPasswordTypeDTO toInvalidPasswordTypeDTO(
            RegisterUserResults.InvalidPasswordType type) {
        if (type == null) {
            throw new IllegalArgumentException("InvalidPasswordType cannot be null");
        }

        return switch (type) {
            case TOO_SHORT -> RegisterUserInvalidPasswordTypeDTO.TOO_SHORT;
            case TOO_LONG -> RegisterUserInvalidPasswordTypeDTO.TOO_LONG;
            case WEAK -> RegisterUserInvalidPasswordTypeDTO.WEAK;
        };
    }

}
