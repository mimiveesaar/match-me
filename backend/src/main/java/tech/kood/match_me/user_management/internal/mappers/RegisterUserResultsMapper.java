package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.api.DTOs.RegisterUserResultsDTO;
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
            case RegisterUserResults.Success success -> new RegisterUserResultsDTO.Success(
                    userMapper.toUserDTO(success.user()), success.tracingId().orElse(null));
            case RegisterUserResults.UsernameExists usernameExists -> new RegisterUserResultsDTO.UsernameExists(
                    usernameExists.username(), usernameExists.tracingId().orElse(null));
            case RegisterUserResults.EmailExists emailExists -> new RegisterUserResultsDTO.EmailExists(
                    emailExists.email(), emailExists.tracingId().orElse(null));
            case RegisterUserResults.InvalidEmail invalidEmail -> new RegisterUserResultsDTO.InvalidEmail(
                    invalidEmail.email(), invalidEmail.tracingId().orElse(null));
            case RegisterUserResults.InvalidPassword invalidPasswordLength -> new RegisterUserResultsDTO.InvalidPassword(
                    invalidPasswordLength.password(),
                    invalidPasswordTypeMapper
                            .toInvalidPasswordTypeDTO(invalidPasswordLength.type()),
                    invalidPasswordLength.tracingId().orElse(null));
            case RegisterUserResults.InvalidUsername invalidUsername -> new RegisterUserResultsDTO.InvalidUsername(
                    invalidUsername.username(),
                    invalidUsernameTypeMapper.toInvalidUsernameType(invalidUsername.type()),
                    invalidUsername.tracingId().orElse(null));
            case RegisterUserResults.SystemError systemError -> new RegisterUserResultsDTO.SystemError(
                    systemError.message(), systemError.tracingId().orElse(null));
        };
    }
}
