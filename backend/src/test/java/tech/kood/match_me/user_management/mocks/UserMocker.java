package tech.kood.match_me.user_management.mocks;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import tech.kood.match_me.user_management.models.User;

public class UserMocker {
    public static Faker faker = new Faker();

    public static User createValidUser() {
        return new User(
            UUID.randomUUID(),
            faker.name().username(),
            faker.internet().password(8, 16),
            faker.internet().emailAddress()
        );
    }
}
