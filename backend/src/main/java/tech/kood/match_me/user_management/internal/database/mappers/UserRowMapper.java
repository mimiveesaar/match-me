package tech.kood.match_me.user_management.internal.database.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import tech.kood.match_me.user_management.internal.entities.UserEntity;


@Component
public class UserRowMapper implements RowMapper<UserEntity> {

    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID id = rs.getObject("id", UUID.class);
        String email = rs.getString("email");
        String password_hash = rs.getString("password_hash");
        String password_salt = rs.getString("password_salt");
        String username = rs.getString("username");
        Instant created_at = rs.getTimestamp("created_at").toInstant();
        Instant updated_at = rs.getTimestamp("updated_at").toInstant();

        return new UserEntity(id, email, username, password_hash, password_salt, created_at, updated_at);
    }
}