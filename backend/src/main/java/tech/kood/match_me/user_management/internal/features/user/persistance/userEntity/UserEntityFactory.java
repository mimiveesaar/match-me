package tech.kood.match_me.user_management.internal.features.user.persistance.userEntity;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.InfrastructureLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.internal.features.user.domain.model.User;

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

    public UserEntity make(UUID id, String email, String passwordHash, String passwordSalt, Instant createdAt, Instant updatedAt) throws CheckedConstraintViolationException {
        var entity = new UserEntity(id, email, passwordHash, passwordSalt, createdAt, updatedAt);
        var validationResult = validator.validate(entity);
        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }
        return entity;
    }

    public UserEntity makeNew(User user) throws CheckedConstraintViolationException {
        return make(user.getId().getValue(), user.getEmail().toString(), user.getHashedPassword().getHash(), user.getHashedPassword().getSalt(), Instant.now(), Instant.now());
    }
}