package tech.kood.match_me.user_management.features.refreshToken.internal.mapper;

import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenIdDTO;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenSecretDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.refreshToken.RefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.refreshToken.RefreshTokenFactory;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.refreshTokenId.RefreshTokenIdFactory;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.refreshTokenSecret.RefreshTokenSecretFactory;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.refreshTokenEntity.RefreshTokenEntity;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.refreshTokenEntity.RefreshTokenEntityFactory;

@Component
@ApplicationLayer
public final class RefreshTokenMapper {

    private final RefreshTokenEntityFactory refreshTokenEntityFactory;
    private final RefreshTokenIdFactory refreshTokenIdFactory;
    private final RefreshTokenFactory refreshTokenFactory;
    private final RefreshTokenSecretFactory refreshTokenSecretFactory;
    private final UserIdFactory userIdFactory;

    public RefreshTokenMapper(RefreshTokenEntityFactory refreshTokenEntityFactory, RefreshTokenIdFactory refreshTokenIdFactory, RefreshTokenFactory refreshTokenFactory, UserIdFactory userIdFactory, RefreshTokenSecretFactory refreshTokenSecretFactory, UserIdFactory userIdFactory1) {
        this.refreshTokenEntityFactory = refreshTokenEntityFactory;
        this.refreshTokenIdFactory = refreshTokenIdFactory;
        this.refreshTokenFactory = refreshTokenFactory;
        this.refreshTokenSecretFactory = refreshTokenSecretFactory;
        this.userIdFactory = userIdFactory1;
    }

    public RefreshToken toRefreshToken(RefreshTokenEntity refreshTokenEntity) throws CheckedConstraintViolationException {

        var refreshTokenId = refreshTokenIdFactory.create(refreshTokenEntity.getId());
        var userId = userIdFactory.create(refreshTokenEntity.getUserId());
        var sharedSecret = refreshTokenSecretFactory.create(refreshTokenEntity.getSecret());

        return refreshTokenFactory.create(refreshTokenId, userId, sharedSecret, refreshTokenEntity.getCreatedAt(), refreshTokenEntity.getExpiresAt());
    }

    public RefreshTokenEntity toEntity(RefreshToken refreshToken) throws CheckedConstraintViolationException {
        return refreshTokenEntityFactory.create(refreshToken.getId().getValue(), refreshToken.getUserId().getValue(), refreshToken.getSecret().getValue().toString(), refreshToken.getCreatedAt(), refreshToken.getExpiresAt());
    }

    public RefreshTokenDTO toDTO(RefreshToken refreshToken) {
        var refreshTokenIdDTO = new RefreshTokenIdDTO(refreshToken.getId().getValue());
        var userIdDTO = new UserIdDTO(refreshToken.getUserId().getValue());
        var sharedSecretDTO = RefreshTokenSecretDTO.of(refreshToken.getSecret().toString());

        return new RefreshTokenDTO(
                refreshTokenIdDTO,
                userIdDTO,
                sharedSecretDTO,
                refreshToken.getCreatedAt(),
                refreshToken.getExpiresAt()
        );
    }

    public RefreshTokenDTO toDTO(RefreshTokenEntity refreshTokenEntity) throws CheckedConstraintViolationException {
       var domainObject = toRefreshToken(refreshTokenEntity);
       return toDTO(domainObject);
    }
}
