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
                                        userMapper.toUserDTO(success.getUser()),
                                        success.getTracingId());
                        case RegisterUserResults.UsernameExists usernameExists -> new RegisterUserResultsDTO.UsernameExists(
                                        usernameExists.getUsername(),
                                        usernameExists.getTracingId());
                        case RegisterUserResults.EmailExists emailExists -> new RegisterUserResultsDTO.EmailExists(
                                        emailExists.getEmail(), emailExists.getTracingId());
                        case RegisterUserResults.InvalidPassword invalidPasswordLength -> new RegisterUserResultsDTO.InvalidPassword(
                                        invalidPasswordLength.getPassword(),
                                        invalidPasswordTypeMapper.toInvalidPasswordTypeDTO(
                                                        invalidPasswordLength.getType()),
                                        invalidPasswordLength.getTracingId());
                        case RegisterUserResults.InvalidUsername invalidUsername -> new RegisterUserResultsDTO.InvalidUsername(
                                        invalidUsername.getUsername(),
                                        invalidUsernameTypeMapper.toInvalidUsernameType(
                                                        invalidUsername.getType()),
                                        invalidUsername.getTracingId());
                        case RegisterUserResults.SystemError systemError -> new RegisterUserResultsDTO.SystemError(
                                        systemError.getMessage(), systemError.getTracingId());
                };
        }
}
