package tech.kood.match_me.user_management.features.registerUser;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.entities.UserEntity;
import tech.kood.match_me.user_management.models.HashedPassword;
import tech.kood.match_me.user_management.utils.EmailValidator;
import tech.kood.match_me.user_management.utils.PasswordUtils;

@Service
public class RegisterUserHandler {

    private final UserRepository userRepository;

    public RegisterUserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
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
            return new RegisterUserResults.InvalidEmail(request.email(), request.tracingId().orElse("unknown"));
        }

        if (userRepository.usernameExists(request.username())) {
            return new RegisterUserResults.UsernameExists(request.username(), request.tracingId().orElse("unknown"));
        }

        if (userRepository.emailExists(request.email())) {
            return new RegisterUserResults.EmailExists(request.email(), request.tracingId().orElse("unknown"));
        }

        if (request.password().length() < 8) {
            return new RegisterUserResults.InvalidPasswordLength(request.password(), request.tracingId().orElse("unknown"));
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
            return new RegisterUserResults.SystemError("Failed to register user: " + e.getMessage(), request.tracingId().orElse("unknown"));
        }

        return new RegisterUserResults.Success(
            userEntity.id().toString(),
            request.tracingId().orElse("unknown")
        );
    }
}