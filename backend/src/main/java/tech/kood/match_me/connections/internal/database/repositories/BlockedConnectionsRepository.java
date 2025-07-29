package tech.kood.match_me.connections.internal.database.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.connections.internal.database.entities.BlockedConnectionEntity;
import tech.kood.match_me.connections.internal.database.mappers.BlockedConnectionRowMapper;

@Repository
public class BlockedConnectionsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BlockedConnectionRowMapper blockedConnectionRowMapper;

    public BlockedConnectionsRepository(
            @Qualifier("connectionsNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate,
            BlockedConnectionRowMapper blockedConnectionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.blockedConnectionRowMapper = blockedConnectionRowMapper;
    }

    /**
     * Saves a blocked connection to the database. If a connection with the same ID already exists,
     * it will be updated.
     *
     * @param blockedConnection The blocked connection to save
     */
    public void save(BlockedConnectionEntity blockedConnection) {
        String sql =
                """
                        INSERT INTO connections.blocked_connections (id, blocker_user_id, blocked_user_id, created_at)
                        VALUES (:id, :blocker_user_id, :blocked_user_id, :created_at)
                        ON CONFLICT (id) DO UPDATE SET
                            blocker_user_id = :blocker_user_id,
                            blocked_user_id = :blocked_user_id,
                            created_at = :created_at
                        """;

        jdbcTemplate.update(sql,
                Map.of("id", blockedConnection.id(), "blocker_user_id",
                        blockedConnection.blockerUserId(), "blocked_user_id",
                        blockedConnection.blockedUserId(), "created_at",
                        Timestamp.from(blockedConnection.createdAt())));
    }

    /**
     * Finds a blocked connection by its ID.
     *
     * @param id The ID of the blocked connection
     * @return Optional containing the blocked connection if found, empty otherwise
     */
    public Optional<BlockedConnectionEntity> findById(UUID id) {
        String sql = "SELECT * FROM connections.blocked_connections WHERE id = :id";

        try {
            BlockedConnectionEntity result =
                    jdbcTemplate.queryForObject(sql, Map.of("id", id), blockedConnectionRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Checks if a user has blocked another user.
     *
     * @param blockerUserId The ID of the user who might have blocked
     * @param blockedUserId The ID of the user who might be blocked
     * @return true if the first user has blocked the second user, false otherwise
     */
    public boolean isBlocked(UUID blockerUserId, UUID blockedUserId) {
        String sql = """
                SELECT COUNT(*) FROM connections.blocked_connections
                WHERE blocker_user_id = :blocker_user_id AND blocked_user_id = :blocked_user_id
                """;
        Integer count = jdbcTemplate.queryForObject(sql,
                Map.of("blocker_user_id", blockerUserId, "blocked_user_id", blockedUserId),
                Integer.class);
        return count != null && count > 0;
    }

    /**
     * Checks if there's any block between two users (bidirectional check). Uses the unique index
     * efficiently by normalizing the user order.
     *
     * @param userId1 The ID of the first user
     * @param userId2 The ID of the second user
     * @return true if either user has blocked the other, false otherwise
     */
    public boolean existsBetweenUsers(UUID userId1, UUID userId2) {
        // Use the same normalization as the unique index for optimal performance
        UUID leastUserId = userId1.compareTo(userId2) < 0 ? userId1 : userId2;
        UUID greatestUserId = userId1.compareTo(userId2) < 0 ? userId2 : userId1;

        String sql = """
                SELECT COUNT(*) FROM connections.blocked_connections
                WHERE (blocker_user_id = :least_user_id AND blocked_user_id = :greatest_user_id)
                   OR (blocker_user_id = :greatest_user_id AND blocked_user_id = :least_user_id)
                """;
        Integer count = jdbcTemplate.queryForObject(sql,
                Map.of("least_user_id", leastUserId, "greatest_user_id", greatestUserId),
                Integer.class);
        return count != null && count > 0;
    }

    /**
     * Finds all users blocked by a specific user.
     *
     * @param blockerUserId The ID of the user who blocked others
     * @return List of blocked connections where the user is the blocker
     */
    public List<BlockedConnectionEntity> findByBlockerUserId(UUID blockerUserId) {
        String sql = """
                SELECT * FROM connections.blocked_connections
                WHERE blocker_user_id = :blocker_user_id
                ORDER BY created_at DESC
                """;
        return jdbcTemplate.query(sql, Map.of("blocker_user_id", blockerUserId),
                blockedConnectionRowMapper);
    }

    /**
     * Finds all blockers of a specific user.
     *
     * @param blockedUserId The ID of the user who was blocked
     * @return List of blocked connections where the user is blocked
     */
    public List<BlockedConnectionEntity> findByBlockedUserId(UUID blockedUserId) {
        String sql = """
                SELECT * FROM connections.blocked_connections
                WHERE blocked_user_id = :blocked_user_id
                ORDER BY created_at DESC
                """;
        return jdbcTemplate.query(sql, Map.of("blocked_user_id", blockedUserId),
                blockedConnectionRowMapper);
    }

    /**
     * Removes a block between two users.
     *
     * @param blockerUserId The ID of the user who blocked
     * @param blockedUserId The ID of the user who was blocked
     * @return true if a block was removed, false if no block existed
     */
    public boolean removeBlock(UUID blockerUserId, UUID blockedUserId) {
        String sql = """
                DELETE FROM connections.blocked_connections
                WHERE blocker_user_id = :blocker_user_id AND blocked_user_id = :blocked_user_id
                """;
        return jdbcTemplate.update(sql,
                Map.of("blocker_user_id", blockerUserId, "blocked_user_id", blockedUserId)) > 0;
    }

    /**
     * Deletes a blocked connection by its ID.
     *
     * @param id The ID of the blocked connection to delete
     * @return true if a connection was deleted, false if no connection was found
     */
    public boolean deleteById(UUID id) {
        String sql = "DELETE FROM connections.blocked_connections WHERE id = :id";
        return jdbcTemplate.update(sql, Map.of("id", id)) > 0;
    }

    /**
     * Deletes all blocked connections involving a specific user (as blocker or blocked).
     *
     * @param userId The ID of the user whose blocked connections should be deleted
     * @return Number of connections deleted
     */
    public int deleteByUserId(UUID userId) {
        String sql = """
                DELETE FROM connections.blocked_connections
                WHERE blocker_user_id = :user_id OR blocked_user_id = :user_id
                """;
        return jdbcTemplate.update(sql, Map.of("user_id", userId));
    }

    /**
     * Counts the total number of blocked connections for a specific user (as blocker or blocked).
     *
     * @param userId The ID of the user
     * @return Total number of blocked connections involving the user
     */
    public int countByUserId(UUID userId) {
        String sql = """
                SELECT COUNT(*) FROM connections.blocked_connections
                WHERE blocker_user_id = :user_id OR blocked_user_id = :user_id
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("user_id", userId), Integer.class);
        return count != null ? count : 0;
    }

    /**
     * Deletes all blocked connections. Use with caution as this operation cannot be undone. This
     * method is primarily intended for testing purposes.
     *
     * @return Number of connections deleted
     */
    public int deleteAll() {
        String sql = "DELETE FROM connections.blocked_connections";
        return jdbcTemplate.update(sql, Map.of());
    }
}
