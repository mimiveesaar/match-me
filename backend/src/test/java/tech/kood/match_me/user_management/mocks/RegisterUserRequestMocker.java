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
        return new RegisterUserRequest(
            faker.name().username(),
            faker.internet().password(8, 16),
            faker.internet().emailAddress(),
            Optional.of(UUID.randomUUID().toString()) // Tracing ID can be null for mock purposes
        );
    }
}