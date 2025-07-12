package tech.kood.match_me.user_management.internal.features.registerUser;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.internal.common.Command;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.entities.UserEntity;
import tech.kood.match_me.user_management.internal.models.HashedPassword;
import tech.kood.match_me.user_management.internal.utils.EmailValidator;
import tech.kood.match_me.user_management.internal.utils.PasswordUtils;

@Service
public class RegisterUserHandler {

    private final UserRepository userRepository;

    public RegisterUserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener
    public void handle(Command<RegisterUserRequest, RegisterUserResults> command) {

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
            request.username(),
            request.email(),
            hashedPassword.password_hash(),
            hashedPassword.password_salt(),
            Instant.now(),
            Instant.now()
        );

        try {
            // Save the user entity to the database.
            userRepository.saveUser(userEntity);
        } catch (Exception e) {
            // Handle any exceptions that occur during saving.
            result.complete(new RegisterUserResults.SystemError("Failed to register user: " + e.getMessage(), request.tracingId()));
        }
    }
}