package tech.kood.match_me.user_management.features.user;

import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;
import tech.kood.match_me.user_management.common.domain.internal.email.EmailFactory;
import tech.kood.match_me.user_management.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.User;
import tech.kood.match_me.user_management.common.domain.internal.userId.UserId;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.UserFactory;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;
import tech.kood.match_me.user_management.features.user.internal.persistance.userEntity.UserEntity;

@Component
public class UserMother {
    public static Faker faker = new Faker();

    private final UserFactory userFactory;

    public UserMother(UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    public User createValidUser() throws CheckedConstraintViolationException {
      return userFactory.newUser(UUID.randomUUID().toString(), faker.internet().password(8, 16));
    }

}
