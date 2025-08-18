package tech.kood.match_me.user_management.features.user.domain.internal.model.userId;

import jakarta.validation.Validator;
import org.jmolecules.ddd.annotation.Factory;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;

import java.util.UUID;


@Factory
public final class UserIdFactory {

    private final Validator validator;

    public UserIdFactory(Validator validator) {
        this.validator = validator;
    }

    public UserId create(UUID value) throws CheckedConstraintViolationException {
        var userId = new UserId(value);
        var validationResult = validator.validate(userId);

        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return userId;
    }
}
