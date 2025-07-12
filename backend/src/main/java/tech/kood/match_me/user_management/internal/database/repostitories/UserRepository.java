package tech.kood.match_me.user_management.internal.database.repostitories;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import tech.kood.match_me.user_management.internal.database.mappers.UserRowMapper;
import tech.kood.match_me.user_management.internal.entities.UserEntity;


@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;
    public UserRepository(
            @Qualifier("userManagementNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate,
            UserRowMapper userRowMapper
        ) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    public Boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM user_management.users WHERE username = :username";
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("username", username), Integer.class);
        return count != null && count > 0;
    }

    public Boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM user_management.users WHERE email = :email";
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("email", email), Integer.class);
        return count != null && count > 0;
    }

    public void deleteAll () {
        String sql = "DELETE FROM user_management.users";
        jdbcTemplate.update(sql, Map.of());
    }

    public Optional<UserEntity> findUserByUsername(String username) {
        String sql = "SELECT * FROM user_management.users WHERE username = :username";
        var result = jdbcTemplate.queryForObject(sql, Map.of("username", username), this.userRowMapper);
        return Optional.ofNullable(result);
    }

    public Optional<UserEntity> findUserByEmail(String email) {
        String sql = "SELECT * FROM user_management.users WHERE email = :email";
        var result = jdbcTemplate.queryForObject(sql, Map.of("email", email), this.userRowMapper);
        return Optional.ofNullable(result);
    }

    public Optional<UserEntity> findUserById(UUID id) {
        String sql = "SELECT * FROM user_management.users WHERE id = :id";
        var result = jdbcTemplate.queryForObject(sql, Map.of("id", id), this.userRowMapper);
        return Optional.ofNullable(result);
    }

    public void saveUser(UserEntity user) {
        String sql = """
            INSERT INTO user_management.users (id, email, username, password_hash, password_salt, created_at, updated_at) 
            VALUES (:id, :email, :username, :password_hash, :password_salt, :created_at, :updated_at)
            ON CONFLICT (id) DO UPDATE
            SET email = :email,
                username = :username,
                password_hash = :password_hash,
                password_salt = :password_salt,
                updated_at = :updated_at
        """;
                           
        jdbcTemplate.update(sql, Map.of(
            "id", user.id(),
            "email", user.email(),
            "username", user.username(),
            "password_hash", user.passwordHash(),
            "password_salt", user.passwordSalt(),
            "created_at", Timestamp.from(user.createdAt()),
            "updated_at", Timestamp.from (user.updatedAt())
        ));
    }
}