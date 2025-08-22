package tech.kood.match_me.user_management.features.user;

import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.User;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.UserFactory;
import tech.kood.match_me.user_management.features.user.utils.passwordFaker.PasswordFaker;

@Component
public class UserMother {
    public static Faker faker = new Faker();

    private final UserFactory userFactory;

    public UserMother(UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    public User createValidUser() throws CheckedConstraintViolationException {
      return userFactory.newUser(faker.internet().emailAddress(), PasswordFaker.generatePassword(8,16,true,true));
    }

}
