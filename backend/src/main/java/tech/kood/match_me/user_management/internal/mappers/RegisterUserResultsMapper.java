package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;

import tech.kood.match_me.user_management.api.DTOs.RegisterUserResultsDTO;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;

@Component
public final class RegisterUserResultsMapper {
 
    private final UserMapper userMapper;

    public RegisterUserResultsMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public RegisterUserResultsDTO toDTO(RegisterUserResults result) {
        if (result == null) {
            throw new IllegalArgumentException("RegisterUserResults cannot be null");
        }

        return switch (result) {
            case RegisterUserResults.Success success -> new RegisterUserResultsDTO.Success(
                userMapper.toUserDTO(success.user()),
                success.tracingId()
            );
            case RegisterUserResults.UsernameExists usernameExists -> new RegisterUserResultsDTO.UsernameExists(
                usernameExists.username(),
                usernameExists.tracingId()
            );
            case RegisterUserResults.EmailExists emailExists -> new RegisterUserResultsDTO.EmailExists(
                emailExists.email(),
                emailExists.tracingId()
            );
            case RegisterUserResults.InvalidEmail invalidEmail -> new RegisterUserResultsDTO.InvalidEmail(
                invalidEmail.email(),
                invalidEmail.tracingId()
            );
            case RegisterUserResults.InvalidPasswordLength invalidPasswordLength -> new RegisterUserResultsDTO.InvalidPasswordLength(
                invalidPasswordLength.password(),
                invalidPasswordLength.tracingId()
            );
            case RegisterUserResults.SystemError systemError -> new RegisterUserResultsDTO.SystemError(
                systemError.message(),
                systemError.tracingId()
            );
        };
    }
}