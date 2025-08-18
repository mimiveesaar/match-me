package tech.kood.match_me.user_management.features.refreshToken.internal.mapper;


import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.api.features.refreshToken.domain.dto.RefreshTokenDTO;
import tech.kood.match_me.user_management.api.features.refreshToken.domain.dto.RefreshTokenIdDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshToken.RefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshToken.RefreshTokenFactory;
import tech.kood.match_me.user_management.features.refreshToken.domain.internal.model.refreshTokenId.RefreshTokenIdFactory;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.refreshTokenEntity.RefreshTokenEntity;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.refreshTokenEntity.RefreshTokenEntityFactory;

@Component
@ApplicationLayer
public final class RefreshTokenMapper {

    private final RefreshTokenEntityFactory refreshTokenEntityFactory;
    private final RefreshTokenIdFactory refreshTokenIdFactory;
    private final RefreshTokenFactory refreshTokenFactory;
    private final UserIdFactory userIdFactory;

    public RefreshTokenMapper(RefreshTokenEntityFactory refreshTokenEntityFactory, RefreshTokenIdFactory refreshTokenIdFactory, RefreshTokenFactory refreshTokenFactory, UserIdFactory userIdFactory) {
        this.refreshTokenEntityFactory = refreshTokenEntityFactory;
        this.refreshTokenIdFactory = refreshTokenIdFactory;
        this.refreshTokenFactory = refreshTokenFactory;
        this.userIdFactory = userIdFactory;
    }

    public RefreshToken toRefreshToken(RefreshTokenEntity refreshTokenEntity) throws CheckedConstraintViolationException {

        var refreshTokenId = refreshTokenIdFactory.make(refreshTokenEntity.getId());
        var userId = userIdFactory.make(refreshTokenEntity.getUserId());

        return refreshTokenFactory.make(refreshTokenId, userId, refreshTokenEntity.getSecret(), refreshTokenEntity.getCreatedAt(), refreshTokenEntity.getExpiresAt());
    }

    public RefreshTokenEntity toEntity(RefreshToken refreshToken) throws CheckedConstraintViolationException {
        return refreshTokenEntityFactory.make(refreshToken.getId().getValue(), refreshToken.getUserId().getValue(), refreshToken.getSecret(), refreshToken.getCreatedAt(), refreshToken.getExpiresAt());
    }

    public RefreshTokenDTO toDTO(RefreshToken refreshToken) {
        var refreshTokenIdDTO = new RefreshTokenIdDTO(refreshToken.getId().getValue());
        var userIdDTO = new UserIdDTO(refreshToken.getUserId().getValue());

        return new RefreshTokenDTO(
                refreshTokenIdDTO,
                userIdDTO,
                refreshToken.getSecret(),
                refreshToken.getCreatedAt(),
                refreshToken.getExpiresAt()
        );
    }
}
