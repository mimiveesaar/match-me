package tech.kood.match_me.connections.database.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import tech.kood.match_me.connections.database.entities.RejectedConnectionEntity;
import tech.kood.match_me.connections.database.mappers.RejectedConnectionRowMapper;

@Repository
public class RejectedConnectionsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RejectedConnectionRowMapper rejectedConnectionRowMapper;

    public RejectedConnectionsRepository(
            @Qualifier("connectionsNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate,
            RejectedConnectionRowMapper rejectedConnectionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rejectedConnectionRowMapper = rejectedConnectionRowMapper;
    }

    /**
     * Saves a rejected connection to the database. If a connection with the same ID already exists,
     * it will be updated.
     *
     * @param rejectedConnection The rejected connection to save
     */
    public void save(RejectedConnectionEntity rejectedConnection) {
        String sql =
                """
                        INSERT INTO connections.rejected_connections (id, sender_user_id, target_user_id, rejected_by_user_id, created_at)
                        VALUES (:id, :sender_user_id, :target_user_id, :rejected_by_user_id, :created_at)
                        ON CONFLICT (id) DO UPDATE SET
                            sender_user_id = :sender_user_id,
                            target_user_id = :target_user_id,
                            rejected_by_user_id = :rejected_by_user_id,
                            created_at = :created_at
                        """;

        jdbcTemplate.update(sql,
                Map.of("id", rejectedConnection.id(), "sender_user_id",
                        rejectedConnection.senderUserId(), "target_user_id",
                        rejectedConnection.targetUserId(), "rejected_by_user_id",
                        rejectedConnection.rejectedByUserId(), "created_at",
                        Timestamp.from(rejectedConnection.createdAt())));
    }

    /**
     * Finds a rejected connection by its ID.
     *
     * @param id The ID of the rejected connection
     * @return Optional containing the rejected connection if found, empty otherwise
     */
    public Optional<RejectedConnectionEntity> findById(UUID id) {
        String sql = "SELECT * FROM connections.rejected_connections WHERE id = :id";

        try {
            RejectedConnectionEntity result =
                    jdbcTemplate.queryForObject(sql, Map.of("id", id), rejectedConnectionRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Checks if a connection has been rejected between two users (bidirectional). Uses the unique
     * index efficiently by normalizing the user order.
     *
     * @param user1 The ID of the first user
     * @param user2 The ID of the second user
     * @return true if a rejection exists between the users, false otherwise
     */
    public boolean existsBetweenUsers(UUID user1, UUID user2) {
        // Use the same normalization as the unique index for optimal performance


        String sql = """
                SELECT COUNT(*) FROM connections.rejected_connections
                WHERE (sender_user_id = :user_1_id AND target_user_id = :user_2_id)
                   OR (sender_user_id = :user_2_id AND target_user_id = :user_1_id)
                """;
        Integer count = jdbcTemplate.queryForObject(sql,
                Map.of("user_1_id", user1, "user_2_id", user2), Integer.class);
        return count != null && count > 0;
    }

    /**
     * Finds all rejected connections sent by a specific user.
     *
     * @param senderUserId The ID of the user who sent the connection requests
     * @return List of rejected connections sent by the user
     */
    public List<RejectedConnectionEntity> findBySenderId(UUID senderId) {
        String sql = """
                SELECT * FROM connections.rejected_connections
                WHERE sender_user_id = :sender_user_id
                ORDER BY created_at DESC
                """;
        return jdbcTemplate.query(sql, Map.of("sender_user_id", senderId),
                rejectedConnectionRowMapper);
    }

    /**
     * Finds all rejected connections received by a specific user.
     *
     * @param targetUserId The ID of the user who received the connection requests
     * @return List of rejected connections received by the user
     */
    public List<RejectedConnectionEntity> findByTargetUserId(UUID targetUserId) {
        String sql = """
                SELECT * FROM connections.rejected_connections
                WHERE target_user_id = :target_user_id
                ORDER BY created_at DESC
                """;
        return jdbcTemplate.query(sql, Map.of("target_user_id", targetUserId),
                rejectedConnectionRowMapper);
    }

    /**
     * Finds all connections rejected by a specific user.
     *
     * @param rejectedByUserId The ID of the user who performed the rejection
     * @return List of connections rejected by the user
     */
    public List<RejectedConnectionEntity> findByRejectedByUserId(UUID rejectedByUserId) {
        String sql = """
                SELECT * FROM connections.rejected_connections
                WHERE rejected_by_user_id = :rejected_by_user_id
                ORDER BY created_at DESC
                """;
        return jdbcTemplate.query(sql, Map.of("rejected_by_user_id", rejectedByUserId),
                rejectedConnectionRowMapper);
    }

    /**
     * Deletes a rejected connection by its ID.
     *
     * @param id The ID of the rejected connection to delete
     * @return true if a connection was deleted, false if no connection was found
     */
    public boolean deleteById(UUID id) {
        String sql = "DELETE FROM connections.rejected_connections WHERE id = :id";
        return jdbcTemplate.update(sql, Map.of("id", id)) > 0;
    }

    /**
     * Deletes all rejected connections involving a specific user (as sender, target, or rejector).
     *
     * @param userId The ID of the user whose rejected connections should be deleted
     * @return Number of connections deleted
     */
    public int deleteByUserId(UUID userId) {
        String sql = """
                DELETE FROM connections.rejected_connections
                WHERE sender_user_id = :user_id
                   OR target_user_id = :user_id
                   OR rejected_by_user_id = :user_id
                """;
        return jdbcTemplate.update(sql, Map.of("user_id", userId));
    }

    /**
     * Counts the total number of rejected connections for a specific user (sent, received, or
     * performed rejection).
     *
     * @param userId The ID of the user
     * @return Total number of rejected connections involving the user
     */
    public int countByUserId(UUID userId) {
        String sql = """
                SELECT COUNT(*) FROM connections.rejected_connections
                WHERE sender_user_id = :user_id
                   OR target_user_id = :user_id
                   OR rejected_by_user_id = :user_id
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("user_id", userId), Integer.class);
        return count != null ? count : 0;
    }

    /**
     * Deletes all rejected connections. Use with caution as this operation cannot be undone. This
     * method is primarily intended for testing purposes.
     *
     * @return Number of connections deleted
     */
    public int deleteAll() {
        String sql = "DELETE FROM connections.rejected_connections";
        return jdbcTemplate.update(sql, Map.of());
    }
}
