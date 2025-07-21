package tech.kood.match_me.user_management.mocks;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;

import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserRequest;

@Component
public class RegisterUserRequestMocker {

    public static Faker faker = new Faker();

    public static RegisterUserRequest createValidRequest() {
        return new RegisterUserRequest(
            UUID.randomUUID(),
            faker.name().username(),
            faker.internet().password(8, 16),
            faker.internet().emailAddress(),
            Optional.of(UUID.randomUUID().toString())
        );
    }

    public static RegisterUserRequest createInvalidUsernameRequest() {
        return createValidRequest().withUsername("invalid username!");
    }

    public static RegisterUserRequest createInvalidPasswordRequest() {
        return createValidRequest().withPassword("short");
    }

    public static RegisterUserRequest createNullRequest() {
        return createValidRequest().withUsername(null).withPassword(null).withEmail(null);
    }

    public static RegisterUserRequest createInvalidEmailRequest() {
        return createValidRequest().withEmail("invalid-email");
    }


    public static RegisterUserRequest createShortUsernameRequest() {
        return createValidRequest().withUsername("1");
    }

    public static RegisterUserRequest createEmptyUsernameRequest() {
        return createValidRequest().withUsername("");
    }

    public static RegisterUserRequest createEmptyPasswordRequest() {
        return createValidRequest().withPassword("");
    }

    public static RegisterUserRequest createEmptyEmailRequest() {
        return createValidRequest().withEmail("");
    }

    public static RegisterUserRequest createLongUsernameRequest() {
        String longUsername = "u".repeat(300);
        return createValidRequest().withUsername(longUsername);
    }

    public static RegisterUserRequest createLongPasswordRequest() {
        String longPassword = "p".repeat(300);
        return createValidRequest().withPassword(longPassword);
    }
}