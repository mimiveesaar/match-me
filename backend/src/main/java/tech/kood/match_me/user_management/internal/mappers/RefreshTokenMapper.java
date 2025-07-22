package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;

import tech.kood.match_me.user_management.internal.entities.RefreshTokenEntity;
import tech.kood.match_me.user_management.models.RefreshToken;

@Component
public final class RefreshTokenMapper {
    
    public RefreshToken toRefreshToken(RefreshTokenEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("RefreshTokenEntity cannot be null");
        }
        
        return new RefreshToken(
            entity.id(),
            entity.userId(),
            entity.token(),
            entity.createdAt(),
            entity.expiresAt()
        );
    }

    public RefreshTokenEntity toEntity(RefreshToken model) {
        if (model == null) {
            throw new IllegalArgumentException("RefreshToken cannot be null");
        }
        
        return new RefreshTokenEntity(
            model.id(),
            model.userId(),
            model.token(),
            model.createdAt(),
            model.expiresAt()
        );
    }
}
