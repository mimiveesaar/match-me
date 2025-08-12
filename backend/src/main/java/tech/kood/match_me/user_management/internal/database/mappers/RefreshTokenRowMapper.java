package tech.kood.match_me.user_management.internal.database.mappers;

import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.internal.database.entities.RefreshTokenEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

@Component
public class RefreshTokenRowMapper implements RowMapper<RefreshTokenEntity> {

    @Override
    public RefreshTokenEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID id = rs.getObject("id", UUID.class);
        UUID userId = rs.getObject("user_id", UUID.class);
        String token = rs.getString("token");
        Instant createdAt = rs.getTimestamp("created_at").toInstant();
        Instant expiresAt = rs.getTimestamp("expires_at").toInstant();

        return RefreshTokenEntity.of(id, userId, token, createdAt, expiresAt);
    }
}
