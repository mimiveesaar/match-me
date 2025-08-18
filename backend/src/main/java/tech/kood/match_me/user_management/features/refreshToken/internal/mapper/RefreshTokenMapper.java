package tech.kood.match_me.user_management.features.refreshToken.internal.mapper;

import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenIdDTO;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.SharedSecretDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshToken.RefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshToken.RefreshTokenFactory;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshTokenId.RefreshTokenIdFactory;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.sharedSecret.SharedSecretFactory;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.refreshTokenEntity.RefreshTokenEntity;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.refreshTokenEntity.RefreshTokenEntityFactory;

@Component
@ApplicationLayer
public final class RefreshTokenMapper {

    private final RefreshTokenEntityFactory refreshTokenEntityFactory;
    private final RefreshTokenIdFactory refreshTokenIdFactory;
    private final RefreshTokenFactory refreshTokenFactory;
    private final SharedSecretFactory sharedSecretFactory;
    private final UserIdFactory userIdFactory;

    public RefreshTokenMapper(RefreshTokenEntityFactory refreshTokenEntityFactory, RefreshTokenIdFactory refreshTokenIdFactory, RefreshTokenFactory refreshTokenFactory, UserIdFactory userIdFactory, SharedSecretFactory sharedSecretFactory, UserIdFactory userIdFactory1) {
        this.refreshTokenEntityFactory = refreshTokenEntityFactory;
        this.refreshTokenIdFactory = refreshTokenIdFactory;
        this.refreshTokenFactory = refreshTokenFactory;
        this.sharedSecretFactory = sharedSecretFactory;
        this.userIdFactory = userIdFactory1;
    }

    public RefreshToken toRefreshToken(RefreshTokenEntity refreshTokenEntity) throws CheckedConstraintViolationException {

        var refreshTokenId = refreshTokenIdFactory.make(refreshTokenEntity.getId());
        var userId = userIdFactory.create(refreshTokenEntity.getUserId());
        var sharedSecret = sharedSecretFactory.create(refreshTokenEntity.getSharedSecret());

        return refreshTokenFactory.make(refreshTokenId, userId, sharedSecret, refreshTokenEntity.getCreatedAt(), refreshTokenEntity.getExpiresAt());
    }

    public RefreshTokenEntity toEntity(RefreshToken refreshToken) throws CheckedConstraintViolationException {
        return refreshTokenEntityFactory.make(refreshToken.getId().getValue(), refreshToken.getUserId().getValue(), refreshToken.getSecret().getValue(), refreshToken.getCreatedAt(), refreshToken.getExpiresAt());
    }

    public RefreshTokenDTO toDTO(RefreshToken refreshToken) {
        var refreshTokenIdDTO = new RefreshTokenIdDTO(refreshToken.getId().getValue());
        var userIdDTO = new UserIdDTO(refreshToken.getUserId().getValue());
        var sharedSecretDTO = SharedSecretDTO.of(refreshToken.getSecret().toString());

        return new RefreshTokenDTO(
                refreshTokenIdDTO,
                userIdDTO,
                sharedSecretDTO,
                refreshToken.getCreatedAt(),
                refreshToken.getExpiresAt()
        );
    }
}
