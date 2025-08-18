package tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.sharedSecret;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;

import java.util.UUID;

@Component
@DomainLayer
@Factory
public final class SharedSecretFactory {

    private final Validator validator;

    public SharedSecretFactory(Validator validator) {
        this.validator = validator;
    }

    public SharedSecret create(UUID value) throws CheckedConstraintViolationException {
        var secret = new SharedSecret(value);
        var validationResult = validator.validate(secret);

        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return secret;
    }

    public SharedSecret create(String value) throws CheckedConstraintViolationException {
        return create(UUID.fromString(value));
    }

    public SharedSecret createNew() throws CheckedConstraintViolationException {
        return create(UUID.randomUUID());
    }
}
