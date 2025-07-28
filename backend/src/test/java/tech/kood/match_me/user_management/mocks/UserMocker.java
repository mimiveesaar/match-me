package tech.kood.match_me.user_management.mocks;

import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;

import tech.kood.match_me.user_management.internal.utils.PasswordUtils;
import tech.kood.match_me.user_management.models.User;

@Component
public class UserMocker {
    public static Faker faker = new Faker();

    public User createValidUser() {
        return new User(UUID.randomUUID(), faker.name().username(), faker.internet().emailAddress(),
                PasswordUtils.encode(faker.internet().password(8, 16)), Instant.now(),
                Instant.now());
    }
}
