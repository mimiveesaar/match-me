package tech.kood.match_me.connections.features.acceptedConnection.internal.persistance;

import org.jmolecules.architecture.layered.InfrastructureLayer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity.AcceptedConnectionEntity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@InfrastructureLayer
@Repository
public class AcceptedConnectionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AcceptedConnectionRowMapper acceptedConnectionRowMapper;

    public AcceptedConnectionRepository(
            @Qualifier("connectionsNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate,
            AcceptedConnectionRowMapper acceptedConnectionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.acceptedConnectionRowMapper = acceptedConnectionRowMapper;
    }

    public void save(AcceptedConnectionEntity acceptedConnectionEntity) {
        String sql =
                """
                        INSERT INTO connections.accepted_connections (id, accepted_by_user_id, accepted_user_id, created_at)
                            VALUES (:id, :accepted_by_user_id, :accepted_user_id, :created_at)
                        """;

        jdbcTemplate.update(sql,
                Map.of("id", acceptedConnectionEntity.getId(),
                        "accepted_by_user_id", acceptedConnectionEntity.getAcceptedByUserId(),
                        "accepted_user_id", acceptedConnectionEntity.getAcceptedUserId(),
                        "created_at", Timestamp.from(acceptedConnectionEntity.getCreatedAt())));
    }

    public Optional<AcceptedConnectionEntity> findById(UUID id) {
        String sql = "SELECT * FROM connections.accepted_connections WHERE id = :id";

        try {
            var result =
                    jdbcTemplate.queryForObject(sql, Map.of("id", id), acceptedConnectionRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<AcceptedConnectionEntity> findByAcceptedByUser(UUID acceptedByUserId) {
        String sql =
                "SELECT * FROM connections.accepted_connections WHERE accepted_by_user_id = :accepted_by_user_id";
        return jdbcTemplate.query(sql, Map.of("accepted_by_user_id", acceptedByUserId),
                acceptedConnectionRowMapper);
    }

    public List<AcceptedConnectionEntity> findByAcceptedUser(UUID acceptedUserId) {
        String sql =
                "SELECT * FROM connections.accepted_connections WHERE accepted_user_id = :accepted_user_id";
        return jdbcTemplate.query(sql, Map.of("accepted_user_id", acceptedUserId),
                acceptedConnectionRowMapper);
    }

    public List<AcceptedConnectionEntity> findByUser(UUID userId) {
        String sql =
                "SELECT * FROM connections.accepted_connections WHERE accepted_by_user_id = :user_id OR accepted_user_id = :user_id";
        return jdbcTemplate.query(sql, Map.of("user_id", userId),
                acceptedConnectionRowMapper);
    }

    public Optional<AcceptedConnectionEntity> findBetweenUsers(UUID userId1, UUID userId2) {
        String sql =
                "SELECT * FROM connections.accepted_connections WHERE (accepted_by_user_id = :user1 AND accepted_user_id = :user2) OR (accepted_by_user_id = :user2 AND accepted_user_id = :user1)";
        try {
            var result = jdbcTemplate.queryForObject(sql,
                    Map.of("user1", userId1, "user2", userId2), acceptedConnectionRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean deleteById(UUID id) {
        String sql = "DELETE FROM connections.accepted_connections WHERE id = :id";
        int rowsAffected = jdbcTemplate.update(sql, Map.of("id", id));
        return rowsAffected > 0;
    }

    public void deleteAll() {
        String sql = "DELETE FROM connections.accepted_connections";
        jdbcTemplate.update(sql, Map.of());
    }

    public List<AcceptedConnectionEntity> findAll() {
        String sql = "SELECT * FROM connections.accepted_connections";
        return jdbcTemplate.query(sql, acceptedConnectionRowMapper);
    }
}
