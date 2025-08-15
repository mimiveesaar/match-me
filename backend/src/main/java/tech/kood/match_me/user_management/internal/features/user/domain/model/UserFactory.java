package tech.kood.match_me.user_management.internal.features.user.domain.model;


import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.Email;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.EmailFactory;
import tech.kood.match_me.user_management.internal.features.user.domain.model.hashedPassword.HashedPassword;
import tech.kood.match_me.user_management.internal.features.user.domain.model.hashedPassword.HashedPasswordFactory;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserId;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserIdFactory;

import java.time.Instant;

@Component
public final class UserFactory {

    private final Validator validator;

    private final UserIdFactory userIdFactory;
    private final EmailFactory emailFactory;
    private final HashedPasswordFactory hashedPasswordFactory;

    public UserFactory(Validator validator, UserIdFactory userIdFactory, EmailFactory emailFactory, HashedPasswordFactory hashedPasswordFactory) {
        this.validator = validator;
        this.userIdFactory = userIdFactory;
        this.emailFactory = emailFactory;
        this.hashedPasswordFactory = hashedPasswordFactory;
    }

    public User of(UserId userId, Email email, HashedPassword hashedPassword, Instant createdAt, Instant updatedAt) throws ConstraintViolationException {
        var user = new User(userId, email, hashedPassword, createdAt, updatedAt);
        var validationResult = validator.validate(user);
        if (!validationResult.isEmpty()) {
            throw new ConstraintViolationException(validationResult);
        }
        return user;
    }

    public User newUser(String email, String password) {
        this.of()
    }

}