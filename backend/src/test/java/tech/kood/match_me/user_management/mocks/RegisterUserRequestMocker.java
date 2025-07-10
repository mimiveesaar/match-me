package tech.kood.match_me.user_management.mocks;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;
import tech.kood.match_me.user_management.features.registerUser.RegisterUserRequest;

@Component
public class RegisterUserRequestMocker {

    public static Faker faker = new Faker();
    
    public static RegisterUserRequest createValidRequest() {
        return createValidRequestBuilder().build();
    }

    public static RegisterUserRequest createInvalidEmailRequest() {
        return createValidRequestBuilder().email("invalid-email").build();
    }

    public static RegisterUserRequest createInvalidUsernameRequest() {
        return createValidRequestBuilder().username("").build();
    }

    public static RegisterUserRequest createInvalidPasswordRequest() {
        return createValidRequestBuilder().password("").build();
    }

    public static RegisterUserRequest createNullRequest() {
        return RegisterUserRequest.builder()
            .username(null)
            .password(null)
            .email(null)
            .tracingId(Optional.empty())
            .build();
    }

    public static RegisterUserRequest.RegisterUserRequestBuilder createValidRequestBuilder() {
        return RegisterUserRequest.builder()
            .username(faker.name().username())
            .password(faker.internet().password(8, 16))
            .email(faker.internet().emailAddress())
            .tracingId(Optional.of(UUID.randomUUID().toString()));
    }
}