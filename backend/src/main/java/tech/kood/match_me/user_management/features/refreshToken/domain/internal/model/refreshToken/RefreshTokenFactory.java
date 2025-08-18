package tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshToken;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshTokenId.RefreshTokenId;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshTokenId.RefreshTokenIdFactory;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.sharedSecret.SharedSecret;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.sharedSecret.SharedSecretFactory;
import tech.kood.match_me.user_management.common.domain.internal.userId.UserId;

import java.time.Instant;

@DomainLayer
@Factory
@Component
public final class RefreshTokenFactory {

    private final Validator validator;
    private final RefreshTokenIdFactory refreshTokenIdFactory;
    private final SharedSecretFactory sharedSecretFactory;

    public RefreshTokenFactory(Validator validator, RefreshTokenIdFactory refreshTokenIdFactory, SharedSecretFactory sharedSecretFactory) {
        this.validator = validator;
        this.refreshTokenIdFactory = refreshTokenIdFactory;
        this.sharedSecretFactory = sharedSecretFactory;
    }

    public RefreshToken make(RefreshTokenId refreshTokenId, UserId userId, SharedSecret sharedSecret, Instant createdAt, Instant expiresAt) throws CheckedConstraintViolationException {
        var refreshToken = new RefreshToken(refreshTokenId, userId, sharedSecret, createdAt, expiresAt);

        var validationResult = validator.validate(refreshToken);
        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return refreshToken;
    }

    public RefreshToken makeNew(UserId userId) throws CheckedConstraintViolationException {
        var refreshTokenId = refreshTokenIdFactory.newId();
        var secret = sharedSecretFactory.createNew();

        return this.make(refreshTokenId, userId, secret, Instant.now(), Instant.now());
    }
}
