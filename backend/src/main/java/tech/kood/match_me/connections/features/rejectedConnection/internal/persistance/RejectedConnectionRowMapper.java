package tech.kood.match_me.connections.features.rejectedConnection.internal.persistance;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionReason;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity.RejectedConnectionEntity;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity.RejectedConnectionEntityFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
public class RejectedConnectionRowMapper implements RowMapper<RejectedConnectionEntity> {

    private final RejectedConnectionEntityFactory rejectedConnectionEntityFactory;

    public RejectedConnectionRowMapper(
            RejectedConnectionEntityFactory rejectedConnectionEntityFactory) {
        this.rejectedConnectionEntityFactory = rejectedConnectionEntityFactory;
    }

    @Override
    public RejectedConnectionEntity mapRow(java.sql.ResultSet rs, int rowNum)
            throws java.sql.SQLException {
        UUID id = rs.getObject("id", UUID.class);
        UUID rejectedByUserId = rs.getObject("rejected_by_user_id", UUID.class);
        UUID rejectedUserId = rs.getObject("rejected_user_id", UUID.class);
        String reasonStr = rs.getString("reason");
        Instant createdAt =
                rs.getTimestamp("created_at").toInstant().truncatedTo(ChronoUnit.SECONDS);

        RejectedConnectionReason reason = RejectedConnectionReason.valueOf(reasonStr);

        try {
            return rejectedConnectionEntityFactory.create(id, rejectedByUserId, rejectedUserId,
                    reason, createdAt);
        } catch (tech.kood.match_me.common.exceptions.CheckedConstraintViolationException e) {
            throw new java.sql.SQLException("Failed to map row to RejectedConnectionEntity", e);
        }
    }
}
