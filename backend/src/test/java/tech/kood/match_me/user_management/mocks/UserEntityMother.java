package tech.kood.match_me.user_management.mocks;

import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;
import tech.kood.match_me.user_management.features.user.internal.persistance.userEntity.UserEntity;
import tech.kood.match_me.user_management.features.user.internal.utils.PasswordUtils;

@Component
public class UserEntityMother {
    public static Faker faker = new Faker();

    public UserEntity createValidUserEntity() {
        var passwordHash = PasswordUtils.encode(faker.internet().password(8, 16));
        return UserEntity.of(UUID.randomUUID(), faker.internet().emailAddress(),
                faker.name().username(), passwordHash.getHash(), passwordHash.getSalt(),
                Instant.now(), Instant.now());
    }
}
