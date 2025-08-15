package tech.kood.match_me.user_management.internal.features.user.domain.model;


import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.Email;
import tech.kood.match_me.user_management.internal.features.user.domain.model.email.EmailFactory;
import tech.kood.match_me.user_management.internal.features.user.domain.model.hashedPassword.HashedPassword;
import tech.kood.match_me.user_management.internal.features.user.domain.model.hashedPassword.HashedPasswordFactory;
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

    public User of(UserId userId, Email email, HashedPassword hashedPassword, Instant createdAt, Instant updatedAt) throws ConstraintViolationException {
        var user = new User(userId, email, hashedPassword, createdAt, updatedAt);
        var validationResult = validator.validate(user);
        if (!validationResult.isEmpty()) {
            throw new ConstraintViolationException(validationResult);
        }
        return user;
    }

    public User newUser(String email, String password) throws ConstraintViolationException {
        var userId = userIdFactory.newId();

        Email unvalidatedEmail = null;
        HashedPassword unvalidatedHashedPassword = null;

        try {
            unvalidatedEmail = emailFactory.of(email);
            var passwordObject = passwordFactory.of(password);
            unvalidatedHashedPassword = hashedPasswordFactory.of(passwordObject);

        } catch (ConstraintViolationException ingnored) {
            // We don't care about the validation result, we just want to create the user anyway.'
            // Validation will take place at an aggregate level.
        }


        return of(userId, unvalidatedEmail, unvalidatedHashedPassword, Instant.now(), Instant.now());
    }

}