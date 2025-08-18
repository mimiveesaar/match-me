package tech.kood.match_me.user_management.features.user.domain.internal.model.user;


import jakarta.validation.Validator;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.user.domain.internal.model.email.Email;
import tech.kood.match_me.user_management.features.user.domain.internal.model.email.EmailFactory;
import tech.kood.match_me.user_management.features.user.domain.internal.model.hashedPassword.HashedPassword;
import tech.kood.match_me.user_management.features.user.domain.internal.model.hashedPassword.HashedPasswordFactory;
import tech.kood.match_me.user_management.features.user.domain.internal.model.password.Password;
import tech.kood.match_me.user_management.features.user.domain.internal.model.password.PasswordFactory;

import java.time.Instant;
import java.util.UUID;

@Component
@Factory
public final class UserFactory {

    private final Validator validator;

    private final EmailFactory emailFactory;
    private final PasswordFactory passwordFactory;
    private final HashedPasswordFactory hashedPasswordFactory;

    public UserFactory(Validator validator, EmailFactory emailFactory, PasswordFactory passwordFactory, HashedPasswordFactory hashedPasswordFactory) {
        this.validator = validator;
        this.emailFactory = emailFactory;
        this.passwordFactory = passwordFactory;
        this.hashedPasswordFactory = hashedPasswordFactory;
    }

    public User make(UserId userId, Email email, HashedPassword hashedPassword, Instant createdAt, Instant updatedAt) throws CheckedConstraintViolationException {
        var user = new User(userId, email, hashedPassword, createdAt, updatedAt);
        var validationResult = validator.validate(user);
        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }
        return user;
    }

    /**
     * Creates a new {@link User} instance with the given email and password.
     * @param email the email address of the new user.
     * @param password clear-text password of the new user.
     * @return a new {@link User} instance
     */
    public User newUser(String email, String password) throws CheckedConstraintViolationException {
        Email validEmail = emailFactory.create(email);
        Password validPassword = passwordFactory.create(password);

        return newUser(validEmail, validPassword);
    }

    public User newUser(Email email, Password password) throws CheckedConstraintViolationException {
        var userId = new UserId(UUID.randomUUID());
        var hashedPassword = hashedPasswordFactory.fromPlainText(password);

        return make(userId, email, hashedPassword, Instant.now(), Instant.now());
    }
}