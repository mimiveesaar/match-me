package tech.kood.match_me.connections.database.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import tech.kood.match_me.connections.database.entities.RejectedConnectionEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Row mapper for mapping database rows to RejectedConnectionEntity objects.
 */
@Component
public class RejectedConnectionRowMapper implements RowMapper<RejectedConnectionEntity> {

    @Override
    public RejectedConnectionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RejectedConnectionEntity(UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("sender_user_id")),
                UUID.fromString(rs.getString("target_user_id")),
                UUID.fromString(rs.getString("rejected_by_user_id")),
                rs.getTimestamp("created_at").toInstant());
    }
}
