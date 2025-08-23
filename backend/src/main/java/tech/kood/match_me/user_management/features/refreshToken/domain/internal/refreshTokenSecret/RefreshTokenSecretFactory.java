package tech.kood.match_me.user_management.features.refreshToken.domain.internal.refreshTokenSecret;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;

import java.util.UUID;

@Component
@DomainLayer
@Factory
public final class RefreshTokenSecretFactory {

    private final Validator validator;

    public RefreshTokenSecretFactory(Validator validator) {
        this.validator = validator;
    }

    public RefreshTokenSecret create(UUID value) throws CheckedConstraintViolationException {
        var secret = new RefreshTokenSecret(value);
        var validationResult = validator.validate(secret);

        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return secret;
    }

    public RefreshTokenSecret create(String value) throws CheckedConstraintViolationException {
        return create(UUID.fromString(value));
    }

    public RefreshTokenSecret createNew() throws CheckedConstraintViolationException {
        return create(UUID.randomUUID());
    }
}
