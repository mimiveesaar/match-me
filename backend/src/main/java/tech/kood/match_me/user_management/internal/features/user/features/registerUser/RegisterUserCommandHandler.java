package tech.kood.match_me.user_management.internal.features.user.features.registerUser;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.UserManagementConfig;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.internal.features.user.domain.model.User;
import tech.kood.match_me.user_management.internal.features.user.domain.model.UserFactory;
import tech.kood.match_me.user_management.internal.features.user.persistance.userEntity.UserEntity;
import tech.kood.match_me.user_management.internal.features.user.persistance.UserRepository;
import tech.kood.match_me.user_management.internal.features.user.domain.model.hashedPassword.HashedPassword;
import tech.kood.match_me.user_management.internal.mappers.UserMapper;
import tech.kood.match_me.user_management.internal.features.user.utils.PasswordUtils;

@Service
public final class RegisterUserCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(RegisterUserCommandHandler.class);
    private static final int BCRYPT_MAX_PASSWORD_LENGTH = 72;
    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final ApplicationEventPublisher events;
    private final UserManagementConfig userManagementConfig;
    private final UserMapper userMapper;
    private final Validator validator;

    public RegisterUserCommandHandler(UserRepository userRepository, UserFactory userFactory, ApplicationEventPublisher events,
                                      UserMapper userMapper, UserManagementConfig userManagementConfig, Validator validator) {

        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.events = events;
        this.userMapper = userMapper;
        this.userManagementConfig = userManagementConfig;
        this.validator = validator;
    }

    public RegisterUserResults handle(RegisterUserRequest request) throws CheckedConstraintViolationException {

        var validationResults = validator.validate(request);
        if (!validationResults.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResults);
        }

        try {

            // Check for email uniqueness.
            if (userRepository.emailExists(request.email().toString())) {
                var result = new RegisterUserResults.EmailExists(request.email(),
                        request.requestId(), request.tracingId());
                events.publishEvent(new RegisterUserEvent(request, result));
                return result;
            }

            var user = userFactory.newUser(request.email(), request.password());

        } catch (Exception e) {
            var result = new RegisterUserResults.SystemError(e.getMessage(), request.requestId(), request.tracingId());
            events.publishEvent(new RegisterUserEvent(request, result));
            return result;
        }



        try {



            // Create a new UserEntity.
            var userEntity = UserEntity.of(UUID.randomUUID(), request.email(),
                    request.username(), hashedPassword.getHash(), hashedPassword.getSalt(),
                    Instant.now(), Instant.now());

            // Save the user entity to the database.
            userRepository.saveUser(userEntity);

            var result = RegisterUserResults.Success.of(request.requestId(),
                    userMapper.toUser(userEntity), request.tracingId());
            events.publishEvent(new RegisterUserEvent(request, result));
            return result;
        } catch (Exception e) {
            // Handle any exceptions that occur during saving.
            var result = RegisterUserResults.SystemError.of(request.requestId(),
                    "Failed to register user: " + e.getMessage(), request.tracingId());
            events.publishEvent(new RegisterUserEvent(request, result));
            return result;
        }
    }
}
