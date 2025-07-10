package tech.kood.match_me.user_management.mocks;

import java.time.Instant;
import com.github.javafaker.Faker;

import tech.kood.match_me.user_management.internal.entities.UserEntity;
import tech.kood.match_me.user_management.internal.utils.PasswordUtils;

public class UserEntityMocker {
    public static Faker faker = new Faker();

    public static UserEntity createValidUserEntity() {
        return createValidUserEntityBuilder().build();
    }

    public static UserEntity.UserEntityBuilder createValidUserEntityBuilder() {
        var user = UserMocker.createValidUser();
        var password_hash = PasswordUtils.encode(user.password());

        return UserEntity.builder()
            .id(user.id())
            .email(user.email())
            .username(user.username())
            .password_hash(password_hash.password_hash())
            .password_salt(password_hash.password_salt())
            .created_at(Instant.now())
            .updated_at(Instant.now());
    }
}