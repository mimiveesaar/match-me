package tech.kood.match_me.user_management.mocks;

import java.time.Instant;
import java.util.UUID;

import com.github.javafaker.Faker;

import tech.kood.match_me.user_management.internal.entities.UserEntity;
import tech.kood.match_me.user_management.internal.utils.PasswordUtils;

public class UserEntityMocker {
    public static Faker faker = new Faker();

    public static UserEntity createValidUserEntity() {
        var passwordHash = PasswordUtils.encode(faker.internet().password(8, 16));
        return new UserEntity(
                UUID.randomUUID(),
                faker.internet().emailAddress(),
                faker.name().username(),
                passwordHash.hash(),
                passwordHash.salt(),
                Instant.now(),
                Instant.now()
            );
    }
}