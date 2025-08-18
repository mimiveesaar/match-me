package tech.kood.match_me.user_management.mocks;

import java.util.UUID;
import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserRequest;

@Component
public class RegisterUserRequestMocker {

    public static Faker faker = new Faker();

    public RegisterUserRequest createValidRequest() {
        return RegisterUserRequest.of(UUID.randomUUID(), faker.name().username(),
                faker.internet().password(8, 16), faker.internet().emailAddress(),
                UUID.randomUUID().toString());
    }

    public RegisterUserRequest createInvalidUsernameRequest() {
        return createValidRequest().withUsername("invalid username!");
    }

    public RegisterUserRequest createInvalidPasswordRequest() {
        return createValidRequest().withPassword("short");
    }

    public RegisterUserRequest createNullRequest() {
        return createValidRequest().withUsername(null).withPassword(null).withEmail(null);
    }

    public RegisterUserRequest createInvalidEmailRequest() {
        return createValidRequest().withEmail("invalid-email");
    }


    public RegisterUserRequest createShortUsernameRequest() {
        return createValidRequest().withUsername("1");
    }

    public RegisterUserRequest createEmptyUsernameRequest() {
        return createValidRequest().withUsername("");
    }

    public RegisterUserRequest createEmptyPasswordRequest() {
        return createValidRequest().withPassword("");
    }

    public RegisterUserRequest createEmptyEmailRequest() {
        return createValidRequest().withEmail("");
    }

    public RegisterUserRequest createLongUsernameRequest() {
        String longUsername = "u".repeat(300);
        return createValidRequest().withUsername(longUsername);
    }

    public RegisterUserRequest createLongPasswordRequest() {
        String longPassword = "p".repeat(300);
        return createValidRequest().withPassword(longPassword);
    }
}
