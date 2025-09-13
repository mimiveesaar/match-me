package tech.kood.match_me.user_management.features.user.internal.persistance;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.jmolecules.architecture.layered.InfrastructureLayer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import tech.kood.match_me.user_management.features.user.internal.persistance.userEntity.UserEntity;

@Repository
@InfrastructureLayer
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public UserRepository(
            @Qualifier("userManagementNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate,
            UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    public Boolean userExists(UUID userId) {
        String sql = "SELECT COUNT(*) FROM user_management.users WHERE id = :userId";
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("userId", userId), Integer.class);
        return count != null && count > 0;
    }

    public Boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM user_management.users WHERE email = :email";
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("email", email), Integer.class);
        return count != null && count > 0;
    }

    public void deleteAll() {
        String sql = "DELETE FROM user_management.users";
        jdbcTemplate.update(sql, Map.of());
    }

    public Optional<UserEntity> findUserByEmail(String email) {
        String sql = "SELECT * FROM user_management.users WHERE email = :email";

        try {
            var result =
                    jdbcTemplate.queryForObject(sql, Map.of("email", email), this.userRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            // Handle the case where no userId is found
            return Optional.empty();
        }
    }

    public Optional<UserEntity> findUserById(UUID id) {
        String sql = "SELECT * FROM user_management.users WHERE id = :id";

        try {
            var result = jdbcTemplate.queryForObject(sql, Map.of("id", id), this.userRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            // Handle the case where no userId is found
            return Optional.empty();
        }
    }

    public void saveUser(UserEntity user) {
        String sql =
                """
                INSERT INTO user_management.users (id, email, password_hash, password_salt, created_at, updated_at)
                VALUES (:id, :email, :password_hash, :password_salt, :created_at, :updated_at)
                ON CONFLICT (id) DO UPDATE
                SET email = :email,
                    password_hash = :password_hash,
                    password_salt = :password_salt,
                    updated_at = :updated_at
                """;

        jdbcTemplate.update(sql,
                Map.of("id", user.getId(),
                        "email", user.getEmail(),
                        "password_hash", user.getPasswordHash(),
                        "password_salt", user.getPasswordSalt(),
                        "created_at", Timestamp.from(user.getCreatedAt()),
                        "updated_at", Timestamp.from(user.getUpdatedAt())));
    }
}
