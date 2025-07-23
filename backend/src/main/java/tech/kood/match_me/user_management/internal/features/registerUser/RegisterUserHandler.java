package tech.kood.match_me.user_management.internal.features.registerUser;

import java.time.Instant;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.UserManagementConfig;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.entities.UserEntity;
import tech.kood.match_me.user_management.internal.mappers.UserMapper;
import tech.kood.match_me.user_management.internal.utils.EmailValidator;
import tech.kood.match_me.user_management.internal.utils.PasswordUtils;
import tech.kood.match_me.user_management.models.HashedPassword;

@Service
public class RegisterUserHandler {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher events;
    private final UserManagementConfig userManagementConfig;
    private final UserMapper userMapper;

    private static final int BCRYPT_MAX_PASSWORD_LENGTH = 72;

    public RegisterUserHandler(
            UserRepository userRepository,
            ApplicationEventPublisher events,
            UserMapper userMapper,
            UserManagementConfig userManagementConfig) {

        this.userRepository = userRepository;
        this.events = events;
        this.userMapper = userMapper;
        this.userManagementConfig = userManagementConfig;
    }

    public RegisterUserResults handle(RegisterUserRequest request) {

        // Validate the request
        if (request.username() == null || request.username().isBlank()) {
            var result = new RegisterUserResults.InvalidUsername(request.username(),
                    RegisterUserResults.InvalidUsernameType.TOO_SHORT, request.tracingId());
            events.publishEvent(new RegisterUserEvent(request, result));
            return result;
        }

        if (request.email() == null || request.email().isBlank()
                || EmailValidator.isValidEmail(request.email()) == false) {

            var result = new RegisterUserResults.InvalidEmail(request.email(), request.tracingId());
            events.publishEvent(
                    new RegisterUserEvent(request, result));
            return result;
        }

        if (request.username() == null || request.username().isBlank()
                || request.username().length() < userManagementConfig.getUsernameMinLength()) {
            var result = new RegisterUserResults.InvalidUsername(request.username(),
                    RegisterUserResults.InvalidUsernameType.TOO_SHORT, request.tracingId());
            events.publishEvent(
                    new RegisterUserEvent(request, result));
            return result;
        } else if (request.username().length() > userManagementConfig.getUsernameMaxLength()) {
            var result = new RegisterUserResults.InvalidUsername(request.username(),
                    RegisterUserResults.InvalidUsernameType.TOO_LONG, request.tracingId());
            events.publishEvent(
                    new RegisterUserEvent(request, result));
            return result;
        } else if (!request.username().matches("^[a-zA-Z0-9_.-]+$")) {
            var result = new RegisterUserResults.InvalidUsername(request.username(),
                    RegisterUserResults.InvalidUsernameType.INVALID_CHARACTERS, request.tracingId());
            events.publishEvent(
                    new RegisterUserEvent(request, result));
            return result;
        }

        if (request.password() == null || request.password().isBlank()
                || request.password().length() < userManagementConfig.getPasswordMinLength()) {
            var result = new RegisterUserResults.InvalidPassword(request.password(),
                    RegisterUserResults.InvalidPasswordType.TOO_SHORT, request.tracingId());
            events.publishEvent(
                    new RegisterUserEvent(request, result));
            return result;
        }

        if (request.password().length() > BCRYPT_MAX_PASSWORD_LENGTH
                || request.password().length() > userManagementConfig.getPasswordMaxLength()) {
            var result = new RegisterUserResults.InvalidPassword(request.password(),
                    RegisterUserResults.InvalidPasswordType.TOO_LONG, request.tracingId());
            events.publishEvent(
                    new RegisterUserEvent(request, result));
            return result;
        }

        if (userRepository.usernameExists(request.username())) {
            var result = new RegisterUserResults.UsernameExists(request.username(), request.tracingId());
            events.publishEvent(
                    new RegisterUserEvent(request, result));
            return result;
        }

        if (userRepository.emailExists(request.email())) {
            var result = new RegisterUserResults.EmailExists(request.email(), request.tracingId());
            events.publishEvent(
                    new RegisterUserEvent(request, result));
            return result;
        }

        // Generate hash from the clear-text password.
        HashedPassword hashedPassword = PasswordUtils.encode(request.password());

        // Create a new UserEntity.
        var userEntity = new UserEntity(
                UUID.randomUUID(),
                request.email(),
                request.username(),
                hashedPassword.hash(),
                hashedPassword.salt(),
                Instant.now(),
                Instant.now());

        try {
            // Save the user entity to the database.
            userRepository.saveUser(userEntity);

            return new RegisterUserResults.Success(userMapper.toUser(userEntity), request.tracingId());
        } catch (Exception e) {
            // Handle any exceptions that occur during saving.
            return new RegisterUserResults.SystemError("Failed to register user: " + e.getMessage(),
                    request.tracingId());
        }
    }
}