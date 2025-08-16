package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;

@Component
public final class RefreshTokenMapper {

    public RefreshToken toRefreshToken(RefreshTokenEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("RefreshTokenEntity cannot be null");
        }

        return RefreshToken.of(entity.getId(), entity.getUserId(), entity.getToken(),
                entity.getCreatedAt(), entity.getExpiresAt());
    }

    public RefreshTokenEntity toEntity(RefreshToken model) {
        if (model == null) {
            throw new IllegalArgumentException("RefreshToken cannot be null");
        }

        return RefreshTokenEntity.of(model.getId(), model.getUserId(), model.getToken(),
                model.getCreatedAt(), model.getExpiresAt());
    }
}
