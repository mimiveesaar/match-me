package tech.kood.match_me.user_management.features.refreshToken.internal.persistance;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.refreshTokenEntity.RefreshTokenEntity;

@Repository
public class RefreshTokenRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RefreshTokenRowMapper refreshTokenRowMapper;

    public RefreshTokenRepository(
            @Qualifier("userManagementNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate,
            RefreshTokenRowMapper refreshTokenRowMapper) {

        this.jdbcTemplate = jdbcTemplate;
        this.refreshTokenRowMapper = refreshTokenRowMapper;
    }

    public void deleteAll() {
        String sql = "DELETE FROM user_management.refresh_tokens";
        jdbcTemplate.update(sql, Map.of());
    }

    public Optional<RefreshTokenEntity> deleteToken(String secret) {
        String sql = "DELETE FROM user_management.refresh_tokens WHERE secret = :secret RETURNING *";
        try {
            RefreshTokenEntity deletedToken = jdbcTemplate.queryForObject(
                sql,
                Map.of("secret", secret),
                refreshTokenRowMapper
            );
            return Optional.ofNullable(deletedToken);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(RefreshTokenEntity refreshToken) {
        String sql = "INSERT INTO user_management.refresh_tokens (id, user_id, secret, created_at, expires_at) "
                + "VALUES (:id, :user_id, :secret, :created_at, :expires_at) "
                + "ON CONFLICT (id) DO UPDATE SET expires_at = :expires_at";

        Map<String, Object> params = Map.of(
                "id", refreshToken.getId().toString(),
                "user_id", refreshToken.getUserId().toString(),
                "secret", refreshToken.getSecret(),
                "created_at", Timestamp.from(refreshToken.getCreatedAt()),
                "expires_at", Timestamp.from(refreshToken.getExpiresAt()));

        jdbcTemplate.update(sql, params);
    }

    public boolean validateToken(String secret, UUID userId, Instant currentTime) {
        String sql = "SELECT COUNT(*) FROM user_management.refresh_tokens WHERE secret = :secret AND user_id = :user_id AND expires_at >= :current_time";
        Integer count = jdbcTemplate.queryForObject(sql, Map.of(
                        "secret", secret,
                        "user_id", userId,
                        "current_time", Timestamp.from(currentTime)),
                Integer.class);
        return count != null && count > 0;
    }

    public void deleteExpiredTokens(Instant currentTime) {
        String sql = "DELETE FROM user_management.refresh_tokens WHERE expires_at <= :current_time";
        jdbcTemplate.update(sql, Map.of("current_time", Timestamp.from(currentTime)));
    }

    public Optional<RefreshTokenEntity> findToken(String secret) {
        String sql = "SELECT * FROM user_management.refresh_tokens WHERE secret = :secret";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
                    Map.of("secret", secret), this.refreshTokenRowMapper));
        } catch (EmptyResultDataAccessException e) {
            // Handle the case where the token is not found
            return Optional.empty();
        }
    }

    public Optional<RefreshTokenEntity> findValidToken(String secret, Instant currentTime) {
        String sql = "SELECT * FROM user_management.refresh_tokens WHERE secret = :secret AND expires_at >= :current_time";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
                    Map.of(
                            "secret", secret,
                            "current_time", Timestamp.from(currentTime)),
                    this.refreshTokenRowMapper));
        } catch (EmptyResultDataAccessException e) {
            // Handle the case where the token is not found or is invalid
            return Optional.empty();
        }
    }
}
