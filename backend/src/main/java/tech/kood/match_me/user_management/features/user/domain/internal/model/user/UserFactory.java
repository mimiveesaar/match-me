package tech.kood.match_me.user_management.features.user.domain.internal.model.user;


import jakarta.validation.Validator;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.common.domain.internal.email.Email;
import tech.kood.match_me.user_management.common.domain.internal.email.EmailFactory;
import tech.kood.match_me.user_management.features.user.domain.internal.model.hashedPassword.HashedPassword;
import tech.kood.match_me.user_management.features.user.domain.internal.model.hashedPassword.HashedPasswordFactory;
import tech.kood.match_me.user_management.common.domain.internal.password.Password;
import tech.kood.match_me.user_management.common.domain.internal.password.PasswordFactory;
import tech.kood.match_me.user_management.common.domain.internal.userId.UserId;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@Factory
public final class UserFactory {

    private final Validator validator;

    private final EmailFactory emailFactory;
    private final PasswordFactory passwordFactory;
    private final HashedPasswordFactory hashedPasswordFactory;
    private final UserIdFactory userIdFactory;

    public UserFactory(Validator validator, EmailFactory emailFactory, PasswordFactory passwordFactory, HashedPasswordFactory hashedPasswordFactory, UserIdFactory userIdFactory) {
        this.validator = validator;
        this.emailFactory = emailFactory;
        this.passwordFactory = passwordFactory;
        this.hashedPasswordFactory = hashedPasswordFactory;
        this.userIdFactory = userIdFactory;
    }

    public User create(UserId userId, Email email, HashedPassword hashedPassword, Instant createdAt, Instant updatedAt) throws CheckedConstraintViolationException {
        var user = new User(userId, email, hashedPassword, createdAt, updatedAt);
        var validationResult = validator.validate(user);
        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }
        return user;
    }

    public User newUser(String email, String password) throws CheckedConstraintViolationException {
        Email validEmail = emailFactory.create(email);
        Password validPassword = passwordFactory.create(password);

        return newUser(validEmail, validPassword);
    }

    public User newUser(Email email, Password password) throws CheckedConstraintViolationException {
        var userId = userIdFactory.createNew();
        var hashedPassword = hashedPasswordFactory.fromPlainText(password);

        return create(userId, email, hashedPassword, Instant.now().truncatedTo(ChronoUnit.SECONDS), Instant.now().truncatedTo(ChronoUnit.SECONDS));
    }
}