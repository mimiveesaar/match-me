package tech.kood.match_me.user_management.features.user.internal.persistance.userEntity;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.InfrastructureLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;

import java.time.Instant;
import java.util.UUID;

@Component
@Factory
@InfrastructureLayer
public class UserEntityFactory {

    private final Validator  validator;

    public UserEntityFactory(Validator validator) {
        this.validator = validator;
    }

    public UserEntity make(UUID id, String email, Integer userStatusCode, String passwordHash, String passwordSalt, Instant createdAt, Instant updatedAt) throws CheckedConstraintViolationException {
        var entity = new UserEntity(id, email, userStatusCode, passwordHash, passwordSalt, createdAt, updatedAt);
        var validationResult = validator.validate(entity);
        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }
        return entity;
    }
}