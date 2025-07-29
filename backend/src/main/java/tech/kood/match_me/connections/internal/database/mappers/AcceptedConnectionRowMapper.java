package tech.kood.match_me.connections.internal.database.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import tech.kood.match_me.connections.internal.database.entities.AcceptedConnectionEntity;

@Component
public class AcceptedConnectionRowMapper implements RowMapper<AcceptedConnectionEntity> {

    @Override
    public AcceptedConnectionEntity mapRow(@NonNull ResultSet rs, @NonNull int rowNum)
            throws SQLException {
        UUID id = rs.getObject("id", UUID.class);
        UUID senderUserId = rs.getObject("sender_user_id", UUID.class);
        UUID targetUserId = rs.getObject("target_user_id", UUID.class);
        Instant createdAt = rs.getTimestamp("created_at").toInstant();

        return new AcceptedConnectionEntity(id, senderUserId, targetUserId, createdAt);
    }
}
