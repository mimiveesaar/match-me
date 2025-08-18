package tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshToken;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.annotation.Factory;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshTokenId.RefreshTokenId;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshTokenId.RefreshTokenIdFactory;
import tech.kood.match_me.user_management.features.user.domain.internal.model.userId.UserId;

import java.time.Instant;
import java.util.UUID;

@DomainLayer
@Factory
public final class RefreshTokenFactory {

    private final Validator validator;
    private final RefreshTokenIdFactory refreshTokenIdFactory;

    public RefreshTokenFactory(Validator validator, RefreshTokenIdFactory refreshTokenIdFactory) {
        this.validator = validator;
        this.refreshTokenIdFactory = refreshTokenIdFactory;
    }

    public RefreshToken make(RefreshTokenId refreshTokenId, UserId userId, String secret, Instant createdAt, Instant expiresAt) throws CheckedConstraintViolationException {
        var refreshToken = new RefreshToken(refreshTokenId, userId, secret, createdAt, expiresAt);

        var validationResult = validator.validate(refreshToken);
        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return refreshToken;
    }

    public RefreshToken makeNew(UserId userId) throws CheckedConstraintViolationException {
        var refreshTokenId = refreshTokenIdFactory.newId();
        return this.make(refreshTokenId, userId, UUID.randomUUID().toString(), Instant.now(), Instant.now());
    }
}
