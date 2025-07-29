package tech.kood.match_me.connections.internal.database.repositories;

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
import tech.kood.match_me.connections.internal.database.entities.AcceptedConnectionEntity;
import tech.kood.match_me.connections.internal.database.mappers.AcceptedConnectionRowMapper;

@Repository
public class AcceptedConnectionsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AcceptedConnectionRowMapper acceptedConnectionRowMapper;

    public AcceptedConnectionsRepository(
            @Qualifier("connectionsNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate,
            AcceptedConnectionRowMapper acceptedConnectionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.acceptedConnectionRowMapper = acceptedConnectionRowMapper;
    }

    /**
     * Saves an accepted connection to the database. If a connection with the same ID already
     * exists, it will be updated.
     *
     * @param acceptedConnection The accepted connection to save
     */
    public void save(AcceptedConnectionEntity acceptedConnection) {
        String sql =
                """
                        INSERT INTO connections.accepted_connections (id, sender_user_id, target_user_id, created_at)
                        VALUES (:id, :sender_user_id, :target_user_id, :created_at)
                        ON CONFLICT (id) DO UPDATE SET
                            sender_user_id = :sender_user_id,
                            target_user_id = :target_user_id,
                            created_at = :created_at
                        """;

        jdbcTemplate.update(sql,
                Map.of("id", acceptedConnection.id(), "sender_user_id",
                        acceptedConnection.senderUserId(), "target_user_id",
                        acceptedConnection.targetUserId(), "created_at",
                        Timestamp.from(acceptedConnection.createdAt())));
    }

    /**
     * Finds an accepted connection by its ID.
     *
     * @param id The ID of the accepted connection
     * @return Optional containing the accepted connection if found, empty otherwise
     */
    public Optional<AcceptedConnectionEntity> findById(UUID id) {
        String sql = "SELECT * FROM connections.accepted_connections WHERE id = :id";

        try {
            AcceptedConnectionEntity result =
                    jdbcTemplate.queryForObject(sql, Map.of("id", id), acceptedConnectionRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Finds all accepted connections sent by a specific user.
     *
     * @param senderUserId The ID of the user who sent the connection requests
     * @return List of accepted connections sent by the user
     */
    public List<AcceptedConnectionEntity> findBySenderUserId(UUID senderUserId) {
        String sql =
                "SELECT * FROM connections.accepted_connections WHERE sender_user_id = :sender_user_id ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, Map.of("sender_user_id", senderUserId),
                acceptedConnectionRowMapper);
    }

    /**
     * Finds all accepted connections received by a specific user.
     *
     * @param targetUserId The ID of the user who received the connection requests
     * @return List of accepted connections received by the user
     */
    public List<AcceptedConnectionEntity> findByTargetUserId(UUID targetUserId) {
        String sql =
                "SELECT * FROM connections.accepted_connections WHERE target_user_id = :target_user_id ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, Map.of("target_user_id", targetUserId),
                acceptedConnectionRowMapper);
    }

    /**
     * Finds all accepted connections between two users (bidirectional).
     *
     * @param userId1 The ID of the first user
     * @param userId2 The ID of the second user
     * @return List of accepted connections between the two users
     */
    public List<AcceptedConnectionEntity> findBetweenUsers(UUID userId1, UUID userId2) {
        String sql = """
                SELECT * FROM connections.accepted_connections
                WHERE (sender_user_id = :user_id_1 AND target_user_id = :user_id_2)
                   OR (sender_user_id = :user_id_2 AND target_user_id = :user_id_1)
                ORDER BY created_at DESC
                """;
        return jdbcTemplate.query(sql, Map.of("user_id_1", userId1, "user_id_2", userId2),
                acceptedConnectionRowMapper);
    }

    /**
     * Checks if an accepted connection exists between two users (bidirectional).
     *
     * @param userId1 The ID of the first user
     * @param userId2 The ID of the second user
     * @return true if an accepted connection exists between the users, false otherwise
     */
    public boolean existsBetweenUsers(UUID userId1, UUID userId2) {
        String sql = """
                SELECT COUNT(*) FROM connections.accepted_connections
                WHERE (sender_user_id = :user_id_1 AND target_user_id = :user_id_2)
                   OR (sender_user_id = :user_id_2 AND target_user_id = :user_id_1)
                """;
        Integer count = jdbcTemplate.queryForObject(sql,
                Map.of("user_id_1", userId1, "user_id_2", userId2), Integer.class);
        return count != null && count > 0;
    }

    /**
     * Deletes an accepted connection by its ID.
     *
     * @param id The ID of the accepted connection to delete
     * @return true if a connection was deleted, false if no connection was found
     */
    public boolean deleteById(UUID id) {
        String sql = "DELETE FROM connections.accepted_connections WHERE id = :id";
        return jdbcTemplate.update(sql, Map.of("id", id)) > 0;
    }

    /**
     * Deletes all accepted connections sent by a specific user.
     *
     * @param senderUserId The ID of the user whose sent connections should be deleted
     * @return Number of connections deleted
     */
    public int deleteBySenderUserId(UUID senderUserId) {
        String sql =
                "DELETE FROM connections.accepted_connections WHERE sender_user_id = :sender_user_id";
        return jdbcTemplate.update(sql, Map.of("sender_user_id", senderUserId));
    }

    /**
     * Deletes all accepted connections received by a specific user.
     *
     * @param targetUserId The ID of the user whose received connections should be deleted
     * @return Number of connections deleted
     */
    public int deleteByTargetUserId(UUID targetUserId) {
        String sql =
                "DELETE FROM connections.accepted_connections WHERE target_user_id = :target_user_id";
        return jdbcTemplate.update(sql, Map.of("target_user_id", targetUserId));
    }

    /**
     * Deletes all accepted connections involving a specific user (as sender or target).
     *
     * @param userId The ID of the user whose connections should be deleted
     * @return Number of connections deleted
     */
    public int deleteByUserId(UUID userId) {
        String sql = """
                DELETE FROM connections.accepted_connections
                WHERE sender_user_id = :user_id OR target_user_id = :user_id
                """;
        return jdbcTemplate.update(sql, Map.of("user_id", userId));
    }

    /**
     * Deletes all accepted connections. Use with caution as this operation cannot be undone. This
     * method is primarily intended for testing purposes.
     *
     * @return Number of connections deleted
     */
    public int deleteAll() {
        String sql = "DELETE FROM connections.accepted_connections";
        return jdbcTemplate.update(sql, Map.of());
    }

    /**
     * Finds all accepted connections in the system. Use with caution in production as this may
     * return a large amount of data.
     *
     * @return List of all accepted connections
     */
    public List<AcceptedConnectionEntity> findAll() {
        String sql = "SELECT * FROM connections.accepted_connections ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, acceptedConnectionRowMapper);
    }

    /**
     * Counts the total number of accepted connections for a specific user (both sent and received).
     *
     * @param userId The ID of the user
     * @return Total number of accepted connections for the user
     */
    public int countByUserId(UUID userId) {
        String sql = """
                SELECT COUNT(*) FROM connections.accepted_connections
                WHERE sender_user_id = :user_id OR target_user_id = :user_id
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("user_id", userId), Integer.class);
        return count != null ? count : 0;
    }

    /**
     * Counts the number of accepted connections sent by a specific user.
     *
     * @param senderUserId The ID of the user who sent the connections
     * @return Number of accepted connections sent by the user
     */
    public int countBySenderUserId(UUID senderUserId) {
        String sql =
                "SELECT COUNT(*) FROM connections.accepted_connections WHERE sender_user_id = :sender_user_id";
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("sender_user_id", senderUserId),
                Integer.class);
        return count != null ? count : 0;
    }

    /**
     * Counts the number of accepted connections received by a specific user.
     *
     * @param targetUserId The ID of the user who received the connections
     * @return Number of accepted connections received by the user
     */
    public int countByTargetUserId(UUID targetUserId) {
        String sql =
                "SELECT COUNT(*) FROM connections.accepted_connections WHERE target_user_id = :target_user_id";
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("target_user_id", targetUserId),
                Integer.class);
        return count != null ? count : 0;
    }

    /**
     * Finds accepted connections created within a specific time range.
     *
     * @param startTime The start time (inclusive)
     * @param endTime The end time (inclusive)
     * @return List of accepted connections created within the time range
     */
    public List<AcceptedConnectionEntity> findByCreatedAtBetween(Instant startTime,
            Instant endTime) {
        String sql = """
                SELECT * FROM connections.accepted_connections
                WHERE created_at >= :start_time AND created_at <= :end_time
                ORDER BY created_at DESC
                """;
        return jdbcTemplate.query(sql, Map.of("start_time", Timestamp.from(startTime), "end_time",
                Timestamp.from(endTime)), acceptedConnectionRowMapper);
    }
}
