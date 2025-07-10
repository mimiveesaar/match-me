package tech.kood.match_me.user_management.database.repostitories;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import tech.kood.match_me.user_management.database.mappers.UserRowMapper;
import tech.kood.match_me.user_management.entities.UserEntity;

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

    public UserEntity findUserByUsername(String username) {
        String sql = "SELECT * FROM user_management.users WHERE username = :username";
        return jdbcTemplate.queryForObject(sql, Map.of("username", username), this.userRowMapper);
    }

    public UserEntity findUserByEmail(String email) {
        String sql = "SELECT * FROM user_management.users WHERE email = :email";
        return jdbcTemplate.queryForObject(sql, Map.of("email", email), this.userRowMapper);
    }

    public UserEntity findUserById(UUID id) {
        String sql = "SELECT * FROM user_management.users WHERE id = :id";
        return jdbcTemplate.queryForObject(sql, Map.of("id", id), this.userRowMapper);
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
            "password_hash", user.password_hash(),
            "password_salt", user.password_salt(),
            "created_at", Timestamp.from(user.created_at()),
            "updated_at", Timestamp.from (user.updated_at())
        ));
    }
}