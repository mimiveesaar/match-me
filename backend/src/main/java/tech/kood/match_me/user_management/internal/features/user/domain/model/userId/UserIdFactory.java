package tech.kood.match_me.user_management.internal.features.user.domain.model.userId;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@DomainLayer
@Factory
public final class UserIdFactory {

    private final Validator validator;

    public UserIdFactory(Validator validator) {
        this.validator = validator;
    }

    public UserId of(UUID id) throws ConstraintViolationException {
        var userId =  new UserId(id);
        var validationResult = validator.validate(userId);

        if (!validationResult.isEmpty()) {
            throw new ConstraintViolationException(validationResult);
        }
        return userId;
    }

    public UserId of(String id) throws ConstraintViolationException {
        return this.of(UUID.fromString(id));
    }

    public UserId newId() {
        return new UserId(UUID.randomUUID());
    }
}