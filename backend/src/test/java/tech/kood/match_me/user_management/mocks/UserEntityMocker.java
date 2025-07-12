package tech.kood.match_me.user_management.mocks;

import java.time.Instant;
import com.github.javafaker.Faker;

import tech.kood.match_me.user_management.internal.entities.UserEntity;

public class UserEntityMocker {
    public static Faker faker = new Faker();

    public static UserEntity createValidUserEntity() {
        return createValidUserEntityBuilder().build();
    }

    public static UserEntity.UserEntityBuilder createValidUserEntityBuilder() {
        var user = UserMocker.createValidUser();

        return UserEntity.builder()
            .id(user.id())
            .email(user.email())
            .username(user.username())
            .passwordHash(user.password().passwordHash())
            .passwordSalt(user.password().passwordSalt())
            .createdAt(Instant.now())
            .updatedAt(Instant.now());
    }
}