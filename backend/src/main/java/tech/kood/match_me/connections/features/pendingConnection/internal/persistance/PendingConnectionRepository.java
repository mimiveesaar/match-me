package tech.kood.match_me.connections.features.pendingConnection.internal.persistance;


import org.jmolecules.architecture.layered.InfrastructureLayer;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@InfrastructureLayer
@Repository
public class PendingConnectionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PendingConnectionRowMapper pendingConnectionRowMapper;

    public PendingConnectionRepository(@Qualifier("connectionsNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate, PendingConnectionRowMapper pendingConnectionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.pendingConnectionRowMapper = pendingConnectionRowMapper;
    }

    public void save(PendingConnectionEntity pendingConnectionEntity)
    {
        String sql = """    
                INSERT INTO connections.pending_connections (id, sender_id, target_id, created_at)
                    VALUES (:id, :sender_id, :target_id, :created_at)
                """;

        jdbcTemplate.update(sql,
                Map.of("id", pendingConnectionEntity.getId(),
                       "sender_id", pendingConnectionEntity.getSenderId(),
                       "target_id", pendingConnectionEntity.getTargetId(),
                       "created_at", Timestamp.from(pendingConnectionEntity.getCreatedAt())));
    }

    public Optional<PendingConnectionEntity> findById(UUID id)
    {
        String sql = "SELECT * FROM connections.pending_connections WHERE id = :id";

        try {
            var result = jdbcTemplate.queryForObject(sql, Map.of("id", id), pendingConnectionRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<PendingConnectionEntity> findBySender(UUID senderId)
    {
        String sql = "SELECT * FROM connections.pending_connections WHERE sender_user_id = :sender_id";
        return jdbcTemplate.query(sql, Map.of("sender_id", senderId), pendingConnectionRowMapper);
    }

    public List<PendingConnectionEntity> findByTarget(UUID targetId)
    {
        String sql = "SELECT * FROM connections.pending_connections WHERE target_user_id = :target_id";
        return jdbcTemplate.query(sql, Map.of("target_id", targetId), pendingConnectionRowMapper);
    }

    public Optional<PendingConnectionEntity> findBySenderAndTarget(UUID senderId, UUID targetId)
    {
        String sql = "SELECT * FROM connections.pending_connections WHERE sender_user_id = :sender_id AND target_user_id = :target_id";

        try {
            var result = jdbcTemplate.queryForObject(sql, Map.of("sender_id", senderId, "target_id", targetId), pendingConnectionRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean deleteById(UUID id)
    {
        String sql = "DELETE FROM connections.pending_connections WHERE id = :id";
        int rowsAffected = jdbcTemplate.update(sql, Map.of("id", id));
        return rowsAffected > 0;
    }

    public boolean deleteOlderThan(Instant timestamp)
    {
        String sql = "DELETE FROM connections.pending_connections WHERE created_at < :timestamp";
        int rowsAffected = jdbcTemplate.update(sql, Map.of("timestamp", Timestamp.from(timestamp)));
        return rowsAffected > 0;
    }

    public void deleteAll()
    {
        String sql = "DELETE FROM connections.pending_connections";
        jdbcTemplate.update(sql, Map.of());
    }

    public List<PendingConnectionEntity> findAll() {
        String sql = "SELECT * FROM connections.pending_connections";
        return jdbcTemplate.query(sql, pendingConnectionRowMapper);
    }
}
