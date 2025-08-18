package tech.kood.match_me.user_management.features.refreshToken.internal.persistance.refreshTokenEntity;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.InfrastructureLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;

import java.time.Instant;
import java.util.UUID;

@Component
@Factory
@InfrastructureLayer
public class RefreshTokenEntityFactory {

    private final Validator validator;

    public RefreshTokenEntityFactory(Validator validator) {
        this.validator = validator;
    }

    public RefreshTokenEntity make(UUID id, UUID userId, UUID secret, Instant createdAt, Instant expiresAt) throws CheckedConstraintViolationException {
        var entity = new RefreshTokenEntity(id, userId, secret, createdAt, expiresAt);

        var validationResult = validator.validate(entity);
        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return entity;
    }
}
