package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.api.DTOs.registerUser.RegisterUserEmailExistsDTO;
import tech.kood.match_me.user_management.api.DTOs.registerUser.RegisterUserInvalidEmailDTO;
import tech.kood.match_me.user_management.api.DTOs.registerUser.RegisterUserInvalidPasswordDTO;
import tech.kood.match_me.user_management.api.DTOs.registerUser.RegisterUserInvalidUsernameDTO;
import tech.kood.match_me.user_management.api.DTOs.registerUser.RegisterUserResultsDTO;
import tech.kood.match_me.user_management.api.DTOs.registerUser.RegisterUserSuccessDTO;
import tech.kood.match_me.user_management.api.DTOs.registerUser.RegisterUserSystemErrorDTO;
import tech.kood.match_me.user_management.api.DTOs.registerUser.RegisterUserUsernameExistsDTO;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;

@Component
public final class RegisterUserResultsMapper {

    private final UserMapper userMapper;
    private final InvalidPasswordTypeMapper invalidPasswordTypeMapper;
    private final InvalidUsernameTypeMapper invalidUsernameTypeMapper;

    public RegisterUserResultsMapper(UserMapper userMapper,
            InvalidPasswordTypeMapper invalidPasswordTypeMapper,
            InvalidUsernameTypeMapper invalidUsernameTypeMapper) {
        this.userMapper = userMapper;
        this.invalidPasswordTypeMapper = invalidPasswordTypeMapper;
        this.invalidUsernameTypeMapper = invalidUsernameTypeMapper;
    }

    public RegisterUserResultsDTO toDTO(RegisterUserResults result) {
        if (result == null) {
            throw new IllegalArgumentException("RegisterUserResults cannot be null");
        }

        return switch (result) {
            case RegisterUserResults.Success success -> new RegisterUserSuccessDTO(
                    userMapper.toUserDTO(success.user()), success.tracingId().orElse(null));
            case RegisterUserResults.UsernameExists usernameExists -> new RegisterUserUsernameExistsDTO(
                    usernameExists.username(), usernameExists.tracingId().orElse(null));
            case RegisterUserResults.EmailExists emailExists -> new RegisterUserEmailExistsDTO(
                    emailExists.email(), emailExists.tracingId().orElse(null));
            case RegisterUserResults.InvalidEmail invalidEmail -> new RegisterUserInvalidEmailDTO(
                    invalidEmail.email(), invalidEmail.tracingId().orElse(null));
            case RegisterUserResults.InvalidPassword invalidPasswordLength -> new RegisterUserInvalidPasswordDTO(
                    invalidPasswordLength.password(),
                    invalidPasswordTypeMapper
                            .toInvalidPasswordTypeDTO(invalidPasswordLength.type()),
                    invalidPasswordLength.tracingId().orElse(null));
            case RegisterUserResults.InvalidUsername invalidUsername -> new RegisterUserInvalidUsernameDTO(
                    invalidUsername.username(),
                    invalidUsernameTypeMapper.toInvalidUsernameType(invalidUsername.type()),
                    invalidUsername.tracingId().orElse(null));
            case RegisterUserResults.SystemError systemError -> new RegisterUserSystemErrorDTO(
                    systemError.message(), systemError.tracingId().orElse(null));
        };
    }
}
