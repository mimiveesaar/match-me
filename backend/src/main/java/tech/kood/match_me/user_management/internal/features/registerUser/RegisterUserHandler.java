package tech.kood.match_me.user_management.internal.features.registerUser;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.internal.common.Command;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.entities.UserEntity;
import tech.kood.match_me.user_management.internal.utils.EmailValidator;
import tech.kood.match_me.user_management.internal.utils.PasswordUtils;
import tech.kood.match_me.user_management.models.HashedPassword;
import tech.kood.match_me.user_management.models.User;

@Service
public class RegisterUserHandler {

    private final UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public RegisterUserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener
    public void handle(RegisterUserCommand command) {

        RegisterUserRequest request = command.getRequest();
        CompletableFuture<RegisterUserResults> result = command.getResultFuture();

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
            result.complete(new RegisterUserResults.InvalidEmail(request.email(), request.tracingId()));
        }

        if (userRepository.usernameExists(request.username())) {
            result.complete(new RegisterUserResults.UsernameExists(request.username(), request.tracingId()));
        }

        if (userRepository.emailExists(request.email())) {
            result.complete(new RegisterUserResults.EmailExists(request.email(), request.tracingId()));
        }

        if (request.password().length() < 8) {
            result.complete(new RegisterUserResults.InvalidPasswordLength(request.password(), request.tracingId()));
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

            result.complete(new RegisterUserResults.Success(user, request.tracingId()));
            var registeredEvent = new UserRegisteredEvent(user, request.tracingId());
            applicationEventPublisher.publishEvent(registeredEvent);

        } catch (Exception e) {
            // Handle any exceptions that occur during saving.
            result.complete(new RegisterUserResults.SystemError("Failed to register user: " + e.getMessage(), request.tracingId()));
        }
    }
}