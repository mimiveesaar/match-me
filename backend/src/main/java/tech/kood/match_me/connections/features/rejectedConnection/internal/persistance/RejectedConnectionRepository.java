package tech.kood.match_me.connections.features.rejectedConnection.internal.persistance;

import org.jmolecules.architecture.layered.InfrastructureLayer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity.RejectedConnectionEntity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@InfrastructureLayer
@Repository
public class RejectedConnectionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RejectedConnectionRowMapper rejectedConnectionRowMapper;

    public RejectedConnectionRepository(
            @Qualifier("connectionsNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate,
            RejectedConnectionRowMapper rejectedConnectionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rejectedConnectionRowMapper = rejectedConnectionRowMapper;
    }

    public void save(RejectedConnectionEntity rejectedConnectionEntity) {
        String sql =
                """
                        INSERT INTO connections.rejected_connections (id, rejected_by_user_id, rejected_user_id, reason, created_at)
                            VALUES (:id, :rejected_by_user_id, :rejected_user_id, :reason, :created_at)
                        """;

        jdbcTemplate.update(sql,
                Map.of("id", rejectedConnectionEntity.getId(),
                        "rejected_by_user_id", rejectedConnectionEntity.getRejectedByUserId(),
                        "rejected_user_id", rejectedConnectionEntity.getRejectedUserId(),
                        "reason", rejectedConnectionEntity.getReason().toString(),
                        "created_at", Timestamp.from(rejectedConnectionEntity.getCreatedAt())));
    }

    public Optional<RejectedConnectionEntity> findById(UUID id) {
        String sql = "SELECT * FROM connections.rejected_connections WHERE id = :id";

        try {
            var result =
                    jdbcTemplate.queryForObject(sql, Map.of("id", id), rejectedConnectionRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<RejectedConnectionEntity> findByRejectedByUser(UUID rejectedByUserId) {
        String sql =
                "SELECT * FROM connections.rejected_connections WHERE rejected_by_user_id = :rejected_by_user_id";
        return jdbcTemplate.query(sql, Map.of("rejected_by_user_id", rejectedByUserId),
                rejectedConnectionRowMapper);
    }

    public List<RejectedConnectionEntity> findByRejectedUser(UUID rejectedUserId) {
        String sql =
                "SELECT * FROM connections.rejected_connections WHERE rejected_user_id = :rejected_user_id";
        return jdbcTemplate.query(sql, Map.of("rejected_user_id", rejectedUserId),
                rejectedConnectionRowMapper);
    }

    public Optional<RejectedConnectionEntity> findBetweenUsers(UUID userId1, UUID userId2) {
        String sql =
                "SELECT * FROM connections.rejected_connections WHERE (rejected_by_user_id = :user1 AND rejected_user_id = :user2) OR (rejected_by_user_id = :user2 AND rejected_user_id = :user1)";
        try {
            var result = jdbcTemplate.queryForObject(sql,
                    Map.of("user1", userId1, "user2", userId2), rejectedConnectionRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean deleteById(UUID id) {
        String sql = "DELETE FROM connections.rejected_connections WHERE id = :id";
        int rowsAffected = jdbcTemplate.update(sql, Map.of("id", id));
        return rowsAffected > 0;
    }

    public void deleteAll() {
        String sql = "DELETE FROM connections.rejected_connections";
        jdbcTemplate.update(sql, Map.of());
    }

    public List<RejectedConnectionEntity> findAll() {
        String sql = "SELECT * FROM connections.rejected_connections";
        return jdbcTemplate.query(sql, rejectedConnectionRowMapper);
    }
}
