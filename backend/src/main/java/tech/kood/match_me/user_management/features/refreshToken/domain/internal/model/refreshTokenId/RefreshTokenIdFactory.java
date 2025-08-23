package tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshTokenId;


import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;

import java.util.UUID;

@Component
@DomainLayer
@Factory
public final class RefreshTokenIdFactory {

    private final Validator validator;

    public RefreshTokenIdFactory(Validator validator) {
        this.validator = validator;
    }

    public RefreshTokenId create(UUID id) throws CheckedConstraintViolationException {
        var refreshTokenId = new RefreshTokenId(id);
        var validationResult = validator.validate(refreshTokenId);

        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return refreshTokenId;
    }

    public RefreshTokenId create(String id) throws CheckedConstraintViolationException {
        return this.create(UUID.fromString(id));
    }

    public RefreshTokenId newId() {
        return new RefreshTokenId(UUID.randomUUID());
    }
}
