package tech.kood.match_me.connections.features.acceptedConnection.internal.persistance;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity.AcceptedConnectionEntity;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity.AcceptedConnectionEntityFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
public class AcceptedConnectionRowMapper implements RowMapper<AcceptedConnectionEntity> {

    private final AcceptedConnectionEntityFactory acceptedConnectionEntityFactory;

    public AcceptedConnectionRowMapper(
            AcceptedConnectionEntityFactory acceptedConnectionEntityFactory) {
        this.acceptedConnectionEntityFactory = acceptedConnectionEntityFactory;
    }

    @Override
    public AcceptedConnectionEntity mapRow(java.sql.ResultSet rs, int rowNum)
            throws java.sql.SQLException {
        UUID id = rs.getObject("id", UUID.class);
        UUID acceptedByUserId = rs.getObject("accepted_by_user_id", UUID.class);
        UUID acceptedUserId = rs.getObject("accepted_user_id", UUID.class);
        Instant createdAt =
                rs.getTimestamp("created_at").toInstant().truncatedTo(ChronoUnit.SECONDS);

        try {
            return acceptedConnectionEntityFactory.create(id, acceptedByUserId, acceptedUserId,
                    createdAt);
        } catch (Exception e) {
            throw new java.sql.SQLException("Failed to map row to AcceptedConnectionEntity", e);
        }
    }
}
