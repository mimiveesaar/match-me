package tech.kood.match_me.connections.features.pendingConnection.internal.persistance;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntity;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntityFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
public class PendingConnectionRowMapper implements RowMapper<PendingConnectionEntity> {

    private final PendingConnectionEntityFactory pendingConnectionEntityFactory;

    public PendingConnectionRowMapper(PendingConnectionEntityFactory pendingConnectionEntityFactory) {
        this.pendingConnectionEntityFactory = pendingConnectionEntityFactory;
    }

    @Override
    public PendingConnectionEntity mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        UUID id = rs.getObject("id", UUID.class);
        UUID senderId = rs.getObject("sender_user_id", UUID.class);
        UUID targetId = rs.getObject("target_user_id", UUID.class);
        Instant createdAt = rs.getTimestamp("created_at").toInstant().truncatedTo(ChronoUnit.SECONDS);

        try {
            return pendingConnectionEntityFactory.create(id, senderId, targetId, createdAt);
        } catch (tech.kood.match_me.common.exceptions.CheckedConstraintViolationException e) {
            throw new java.sql.SQLException("Failed to map row to PendingConnectionEntity", e);
        }
    }
}
