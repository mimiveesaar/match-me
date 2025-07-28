package tech.kood.match_me.connections.database.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import tech.kood.match_me.connections.database.entities.PendingConnectionEntity;

@Component
public class PendingConnectionRowMapper implements RowMapper<PendingConnectionEntity> {

    @Override
    public PendingConnectionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID id = rs.getObject("id", UUID.class);
        UUID senderUserId = rs.getObject("sender_user_id", UUID.class);
        UUID targetUserId = rs.getObject("target_user_id", UUID.class);
        Instant createdAt = rs.getTimestamp("created_at").toInstant();

        return new PendingConnectionEntity(id, senderUserId, targetUserId, createdAt);
    }
}
