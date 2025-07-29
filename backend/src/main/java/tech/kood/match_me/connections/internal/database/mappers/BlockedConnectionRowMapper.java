package tech.kood.match_me.connections.internal.database.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import tech.kood.match_me.connections.internal.database.entities.BlockedConnectionEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Row mapper for mapping database rows to BlockedConnectionEntity objects.
 */
@Component
public class BlockedConnectionRowMapper implements RowMapper<BlockedConnectionEntity> {

    @Override
    public BlockedConnectionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BlockedConnectionEntity(UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("blocker_user_id")),
                UUID.fromString(rs.getString("blocked_user_id")),
                rs.getTimestamp("created_at").toInstant());
    }
}
