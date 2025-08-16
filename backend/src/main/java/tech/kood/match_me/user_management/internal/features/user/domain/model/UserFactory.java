package tech.kood.match_me.user_management.internal.features.user.domain.model;


import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.Email;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.EmailFactory;
import tech.kood.match_me.user_management.internal.features.user.domain.model.hashedPassword.HashedPassword;
import tech.kood.match_me.user_management.internal.features.user.domain.model.hashedPassword.HashedPasswordFactory;
import tech.kood.match_me.user_management.internal.features.user.domain.model.password.Password;
import tech.kood.match_me.user_management.internal.features.user.domain.model.password.PasswordFactory;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserId;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserIdFactory;

import java.time.Instant;

@Component
@Factory
public final class UserFactory {

    private final Validator validator;

    private final UserIdFactory userIdFactory;
    private final EmailFactory emailFactory;
    private final PasswordFactory passwordFactory;
    private final HashedPasswordFactory hashedPasswordFactory;

    public UserFactory(Validator validator, UserIdFactory userIdFactory, EmailFactory emailFactory, PasswordFactory passwordFactory, HashedPasswordFactory hashedPasswordFactory) {
        this.validator = validator;
        this.userIdFactory = userIdFactory;
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
        Email unvalidatedEmail = null;
        Password unvalidatedPassword = null;

        try {
            unvalidatedEmail = emailFactory.make(email);
            unvalidatedPassword = passwordFactory.make(password);

        } catch (ConstraintViolationException ignored) {
            // We don't care about the validation result, we want to create the user anyway.
            // Validation will take place at the aggregate level.
        }

        return newUser(unvalidatedEmail, unvalidatedPassword);
    }

    public User newUser(Email email, Password password) throws CheckedConstraintViolationException {
        var userId = userIdFactory.newId();
        HashedPassword unvalidatedHashedPassword = null;

        try {
            unvalidatedHashedPassword = hashedPasswordFactory.fromPassword(password);
        } catch (ConstraintViolationException ignored) {
            // We don't care about the validation result, we want to create the user anyway.
            // Validation will take place at the aggregate level.
        }

        return make(userId, email, unvalidatedHashedPassword, Instant.now(), Instant.now());
    }

}