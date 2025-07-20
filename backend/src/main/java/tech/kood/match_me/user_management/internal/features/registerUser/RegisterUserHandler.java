package tech.kood.match_me.user_management.internal.features.registerUser;

import java.time.Instant;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.entities.UserEntity;
import tech.kood.match_me.user_management.internal.utils.EmailValidator;
import tech.kood.match_me.user_management.internal.utils.PasswordUtils;
import tech.kood.match_me.user_management.models.HashedPassword;
import tech.kood.match_me.user_management.models.User;

@Service
public class RegisterUserHandler {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher events;

    public RegisterUserHandler(UserRepository userRepository, ApplicationEventPublisher events) {
        this.userRepository = userRepository;
        this.events = events;
    }

    public RegisterUserResults handle(RegisterUserRequest request) {


        // Validate the request
        if (request.username() == null || request.username().isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }

        if (EmailValidator.isValidEmail(request.email()) == false) {

            var result = new RegisterUserResults.InvalidEmail(request.email(), request.tracingId());
            events.publishEvent(
                new RegisterUserEvent(request, result)
            );
            return result;
        }

        if (userRepository.usernameExists(request.username())) {
            var result = new RegisterUserResults.UsernameExists(request.username(), request.tracingId());
            events.publishEvent(
                new RegisterUserEvent(request, result)
            );
            return result;
        }

        if (userRepository.emailExists(request.email())) {
            var result = new RegisterUserResults.EmailExists(request.email(), request.tracingId());
            events.publishEvent(
                new RegisterUserEvent(request, result)
            );
            return result;
        }

        if (request.password().length() < 8) {
            var result = new RegisterUserResults.InvalidPasswordLength(request.password(), request.tracingId());
            events.publishEvent(
                new RegisterUserEvent(request, result)
            );
            return result;
        }

        // Generate hash from the clear-text password.
        HashedPassword hashedPassword = PasswordUtils.encode(request.password());

        // Create a new UserEntity.
        var userEntity = new UserEntity(
            UUID.randomUUID(),
            request.email(),
            request.username(),
            hashedPassword.passwordHash(),
            hashedPassword.passwordSalt(),
            Instant.now(),
            Instant.now()
        );

        try {
            // Save the user entity to the database.
            userRepository.saveUser(userEntity);
            var user = new User(
                userEntity.id(),
                userEntity.username(),
                userEntity.email(),
                new HashedPassword(userEntity.passwordHash(), userEntity.passwordSalt()),
                userEntity.createdAt(),
                userEntity.updatedAt()
            );

            return new RegisterUserResults.Success(user, request.tracingId());
        } catch (Exception e) {
            // Handle any exceptions that occur during saving.
            return new RegisterUserResults.SystemError("Failed to register user: " + e.getMessage(), request.tracingId());
        }
    }
}