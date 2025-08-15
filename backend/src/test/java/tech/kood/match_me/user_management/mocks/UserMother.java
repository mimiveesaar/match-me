package tech.kood.match_me.user_management.mocks;

import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;
import tech.kood.match_me.user_management.internal.features.user.domain.model.User;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserId;
import tech.kood.match_me.user_management.internal.features.user.utils.PasswordUtils;

@Component
public class UserMother {
    public static Faker faker = new Faker();

    public User createValidUser() {
        return User.of(UserId.of(UUID.randomUUID()), faker.name().username(),
                faker.internet().emailAddress(),
                PasswordUtils.encode(faker.internet().password(8, 16)), Instant.now(),
                Instant.now());
    }
}
