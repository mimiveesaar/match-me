package tech.kood.match_me.user_management.mocks;

import java.time.Instant;
import com.github.javafaker.Faker;
import tech.kood.match_me.user_management.entities.UserEntity;
import tech.kood.match_me.user_management.utils.PasswordUtils;

public class UserEntityMocker {
    public static Faker faker = new Faker();

    public static UserEntity createValidUserEntity() {
        var user = UserMocker.createValidUser();
        var password_hash = PasswordUtils.encode(user.password());

        return new UserEntity(
            user.id(),
            user.email(),
            user.username(),
            password_hash.password_hash(),
            password_hash.password_salt(),
            Instant.now(),
            Instant.now()
        );
    }
}