package tech.kood.match_me.user_management.internal.features.registerUser;

import java.time.Instant;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.UserManagementConfig;
import tech.kood.match_me.user_management.internal.common.cqrs.QueryHandler;
import tech.kood.match_me.user_management.internal.database.entities.UserEntity;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.domain.models.HashedPassword;
import tech.kood.match_me.user_management.internal.mappers.UserMapper;
import tech.kood.match_me.user_management.internal.utils.PasswordUtils;

@Service
public final class RegisterUserHandler
        implements QueryHandler<RegisterUserRequest, RegisterUserResults> {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher events;
    private final UserManagementConfig userManagementConfig;
    private final UserMapper userMapper;

    private static final int BCRYPT_MAX_PASSWORD_LENGTH = 72;

    public RegisterUserHandler(UserRepository userRepository, ApplicationEventPublisher events,
            UserMapper userMapper, UserManagementConfig userManagementConfig) {

        this.userRepository = userRepository;
        this.events = events;
        this.userMapper = userMapper;
        this.userManagementConfig = userManagementConfig;
    }

    /**
     * Handles the registration of a new user based on the provided request.
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Validates the username, password, and email according to business rules</li>
     * <li>Checks for username and email uniqueness</li>
     * <li>Hashes the password</li>
     * <li>Creates and saves a new user entity</li>
     * <li>Returns appropriate results based on the outcome</li>
     * <li>Publishes a {@code RegisterUserEvent} with the request and result at each step</li>
     * </ul>
     *
     * @param request the {@link RegisterUserRequest} containing user registration details
     * @return a {@link RegisterUserResults} representing the outcome of the operation
     */
    public RegisterUserResults handle(RegisterUserRequest request) {
        try {
            // Validate username length
            if (request.getUsername().length() < userManagementConfig.getUsernameMinLength()) {
                var result = RegisterUserResults.InvalidUsername.of(request.getRequestId(),
                        request.getUsername(), RegisterUserResults.InvalidUsernameType.TOO_SHORT,
                        request.getTracingId());
                events.publishEvent(new RegisterUserEvent(request, result));
                return result;
            } else if (request.getUsername().length() > userManagementConfig
                    .getUsernameMaxLength()) {
                var result = RegisterUserResults.InvalidUsername.of(request.getRequestId(),
                        request.getUsername(), RegisterUserResults.InvalidUsernameType.TOO_LONG,
                        request.getTracingId());
                events.publishEvent(new RegisterUserEvent(request, result));
                return result;
            } else if (!request.getUsername().matches("^[a-zA-Z0-9_.-]+$")) {
                var result = RegisterUserResults.InvalidUsername.of(request.getRequestId(),
                        request.getUsername(),
                        RegisterUserResults.InvalidUsernameType.INVALID_CHARACTERS,
                        request.getTracingId());
                events.publishEvent(new RegisterUserEvent(request, result));
                return result;
            }

            // Validate password length
            if (request.getPassword().length() < userManagementConfig.getPasswordMinLength()) {
                var result = RegisterUserResults.InvalidPassword.of(request.getRequestId(),
                        request.getPassword(), RegisterUserResults.InvalidPasswordType.TOO_SHORT,
                        request.getTracingId());
                events.publishEvent(new RegisterUserEvent(request, result));
                return result;
            }

            if (request.getPassword().length() > BCRYPT_MAX_PASSWORD_LENGTH || request.getPassword()
                    .length() > userManagementConfig.getPasswordMaxLength()) {
                var result = RegisterUserResults.InvalidPassword.of(request.getRequestId(),
                        request.getPassword(), RegisterUserResults.InvalidPasswordType.TOO_LONG,
                        request.getTracingId());
                events.publishEvent(new RegisterUserEvent(request, result));
                return result;
            }

            // Check for username uniqueness
            if (userRepository.usernameExists(request.getUsername())) {
                var result = RegisterUserResults.UsernameExists.of(request.getRequestId(),
                        request.getUsername(), request.getTracingId());
                events.publishEvent(new RegisterUserEvent(request, result));
                return result;
            }

            // Check for email uniqueness
            if (userRepository.emailExists(request.getEmail())) {
                var result = RegisterUserResults.EmailExists.of(request.getRequestId(),
                        request.getEmail(), request.getTracingId());
                events.publishEvent(new RegisterUserEvent(request, result));
                return result;
            }

            // Generate hash from the clear-text password.
            HashedPassword hashedPassword = PasswordUtils.encode(request.getPassword());

            // Create a new UserEntity.
            var userEntity = UserEntity.of(UUID.randomUUID(), request.getEmail(),
                    request.getUsername(), hashedPassword.getHash(), hashedPassword.getSalt(),
                    Instant.now(), Instant.now());

            // Save the user entity to the database.
            userRepository.saveUser(userEntity);

            var result = RegisterUserResults.Success.of(request.getRequestId(),
                    userMapper.toUser(userEntity), request.getTracingId());
            events.publishEvent(new RegisterUserEvent(request, result));
            return result;
        } catch (Exception e) {
            // Handle any exceptions that occur during saving.
            var result = RegisterUserResults.SystemError.of(request.getRequestId(),
                    "Failed to register user: " + e.getMessage(), request.getTracingId());
            events.publishEvent(new RegisterUserEvent(request, result));
            return result;
        }
    }
}
