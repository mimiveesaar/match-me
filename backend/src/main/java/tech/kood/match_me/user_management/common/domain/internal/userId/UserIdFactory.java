package tech.kood.match_me.user_management.common.domain.internal.userId;

import jakarta.validation.Validator;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;

import java.util.UUID;


@Factory
@Component
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

    public UserId from(UserIdDTO userId) throws CheckedConstraintViolationException {
        return create(userId.value());
    }

    public UserId createNew() {
        return new UserId(UUID.randomUUID());
    }
}
