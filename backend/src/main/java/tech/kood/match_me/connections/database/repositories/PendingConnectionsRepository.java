package tech.kood.match_me.connections.database.repositories;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import tech.kood.match_me.connections.database.entities.PendingConnectionEntity;
import tech.kood.match_me.connections.database.mappers.PendingConnectionRowMapper;

@Repository
public class PendingConnectionsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PendingConnectionRowMapper pendingConnectionRowMapper;

    public PendingConnectionsRepository(
            @Qualifier("connectionsNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate,
            PendingConnectionRowMapper pendingConnectionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.pendingConnectionRowMapper = pendingConnectionRowMapper;
    }

    /**
     * Saves a pending connection to the database. If a connection with the same ID already exists,
     * it will be updated.
     *
     * @param pendingConnection The pending connection to save
     */
    public void save(PendingConnectionEntity pendingConnection) {
        String sql =
                """
                        INSERT INTO connections.pending_connections (id, sender_user_id, target_user_id, created_at)
                        VALUES (:id, :sender_user_id, :target_user_id, :created_at)
                        ON CONFLICT (id) DO UPDATE SET
                            sender_user_id = :sender_user_id,
                            target_user_id = :target_user_id,
                            created_at = :created_at
                        """;

        jdbcTemplate.update(sql,
                Map.of("id", pendingConnection.id(), "sender_user_id",
                        pendingConnection.senderUserId(), "target_user_id",
                        pendingConnection.targetUserId(), "created_at",
                        Timestamp.from(pendingConnection.createdAt())));
    }

    /**
     * Finds a pending connection by its ID.
     *
     * @param id The ID of the pending connection
     * @return Optional containing the pending connection if found, empty otherwise
     */
    public Optional<PendingConnectionEntity> findById(UUID id) {
        String sql = "SELECT * FROM connections.pending_connections WHERE id = :id";

        try {
            var result = jdbcTemplate.queryForObject(sql, Map.of("id", id),
                    this.pendingConnectionRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Finds all pending connections sent by a specific user.
     *
     * @param senderUserId The ID of the user who sent the connection requests
     * @return List of pending connections sent by the user
     */
    public List<PendingConnectionEntity> findBySenderUserId(UUID senderUserId) {
        String sql =
                "SELECT * FROM connections.pending_connections WHERE sender_user_id = :sender_user_id ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, Map.of("sender_user_id", senderUserId),
                this.pendingConnectionRowMapper);
    }

    /**
     * Finds all pending connections received by a specific user.
     *
     * @param targetUserId The ID of the user who received the connection requests
     * @return List of pending connections received by the user
     */
    public List<PendingConnectionEntity> findByTargetUserId(UUID targetUserId) {
        String sql =
                "SELECT * FROM connections.pending_connections WHERE target_user_id = :target_user_id ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, Map.of("target_user_id", targetUserId),
                this.pendingConnectionRowMapper);
    }

    /**
     * Finds a pending connection between two specific users.
     *
     * @param senderUserId The ID of the user who sent the connection request
     * @param targetUserId The ID of the user who received the connection request
     * @return Optional containing the pending connection if found, empty otherwise
     */
    public Optional<PendingConnectionEntity> findBySenderAndTarget(UUID senderUserId,
            UUID targetUserId) {
        String sql =
                "SELECT * FROM connections.pending_connections WHERE sender_user_id = :sender_user_id AND target_user_id = :target_user_id";

        try {
            var result = jdbcTemplate.queryForObject(sql,
                    Map.of("sender_user_id", senderUserId, "target_user_id", targetUserId),
                    this.pendingConnectionRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Checks if a pending connection exists between two users (in either direction).
     *
     * @param userId1 The ID of the first user
     * @param userId2 The ID of the second user
     * @return true if a pending connection exists between the users, false otherwise
     */
    public boolean existsBetweenUsers(UUID userId1, UUID userId2) {
        String sql = """
                SELECT COUNT(*) FROM connections.pending_connections
                WHERE (sender_user_id = :user_id_1 AND target_user_id = :user_id_2)
                   OR (sender_user_id = :user_id_2 AND target_user_id = :user_id_1)
                """;

        Integer count = jdbcTemplate.queryForObject(sql,
                Map.of("user_id_1", userId1, "user_id_2", userId2), Integer.class);
        return count != null && count > 0;
    }

    /**
     * Deletes a pending connection by its ID.
     *
     * @param id The ID of the pending connection to delete
     * @return true if the connection was deleted, false if it didn't exist
     */
    public boolean deleteById(UUID id) {
        String sql = "DELETE FROM connections.pending_connections WHERE id = :id";
        return jdbcTemplate.update(sql, Map.of("id", id)) > 0;
    }

    /**
     * Deletes a pending connection between specific users.
     *
     * @param senderUserId The ID of the user who sent the connection request
     * @param targetUserId The ID of the user who received the connection request
     * @return true if the connection was deleted, false if it didn't exist
     */
    public boolean deleteBySenderAndTarget(UUID senderUserId, UUID targetUserId) {
        String sql =
                "DELETE FROM connections.pending_connections WHERE sender_user_id = :sender_user_id AND target_user_id = :target_user_id";
        return jdbcTemplate.update(sql,
                Map.of("sender_user_id", senderUserId, "target_user_id", targetUserId)) > 0;
    }

    /**
     * Deletes all pending connections for a specific user (both sent and received).
     *
     * @param userId The ID of the user
     * @return The number of pending connections deleted
     */
    public int deleteAllForUser(UUID userId) {
        String sql =
                "DELETE FROM connections.pending_connections WHERE sender_user_id = :user_id OR target_user_id = :user_id";
        return jdbcTemplate.update(sql, Map.of("user_id", userId));
    }

    /**
     * Deletes all pending connections created before a specific timestamp.
     *
     * @param cutoffTime The cutoff timestamp
     * @return The number of pending connections deleted
     */
    public int deleteOlderThan(Instant cutoffTime) {
        String sql = "DELETE FROM connections.pending_connections WHERE created_at < :cutoff_time";
        return jdbcTemplate.update(sql, Map.of("cutoff_time", Timestamp.from(cutoffTime)));
    }

    /**
     * Counts all pending connections sent by a specific user.
     *
     * @param senderUserId The ID of the user who sent the connection requests
     * @return The count of pending connections sent by the user
     */
    public long countBySenderUserId(UUID senderUserId) {
        String sql =
                "SELECT COUNT(*) FROM connections.pending_connections WHERE sender_user_id = :sender_user_id";
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("sender_user_id", senderUserId),
                Integer.class);
        return count != null ? count.longValue() : 0L;
    }

    /**
     * Counts all pending connections received by a specific user.
     *
     * @param targetUserId The ID of the user who received the connection requests
     * @return The count of pending connections received by the user
     */
    public long countByTargetUserId(UUID targetUserId) {
        String sql =
                "SELECT COUNT(*) FROM connections.pending_connections WHERE target_user_id = :target_user_id";
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("target_user_id", targetUserId),
                Integer.class);
        return count != null ? count.longValue() : 0L;
    }

    /**
     * Deletes all pending connections from the database. This method should be used with caution,
     * primarily for testing purposes.
     */
    public void deleteAll() {
        String sql = "DELETE FROM connections.pending_connections";
        jdbcTemplate.update(sql, Map.of());
    }

    /**
     * Finds all pending connections in the database. This method should be used with caution as it
     * may return a large amount of data.
     *
     * @return List of all pending connections
     */
    public List<PendingConnectionEntity> findAll() {
        String sql = "SELECT * FROM connections.pending_connections ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, Map.of(), this.pendingConnectionRowMapper);
    }
}
