package tech.kood.match_me.user_management.features.refreshToken.internal.persistance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.refreshTokenEntity.RefreshTokenEntity;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.refreshTokenEntity.RefreshTokenEntityFactory;

@Component
public class RefreshTokenRowMapper implements RowMapper<RefreshTokenEntity> {

    private final RefreshTokenEntityFactory refreshTokenEntityFactory;

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenRowMapper.class);

    public RefreshTokenRowMapper(RefreshTokenEntityFactory refreshTokenEntityFactory) {
        this.refreshTokenEntityFactory = refreshTokenEntityFactory;
    }

    @Override
    public RefreshTokenEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID id = rs.getObject("id", UUID.class);
        UUID userId = rs.getObject("user_id", UUID.class);
        String sharedSecret = rs.getString("shared_secret");
        Instant createdAt = rs.getTimestamp("created_at").toInstant();
        Instant expiresAt = rs.getTimestamp("expires_at").toInstant();

        try {
            return refreshTokenEntityFactory.make(id, userId, UUID.fromString(sharedSecret), createdAt, expiresAt);
        } catch (CheckedConstraintViolationException e) {
            logger.error("Failed to map refresh token row to entity {}", e.getMessage());
            return null;
        }
    }
}
