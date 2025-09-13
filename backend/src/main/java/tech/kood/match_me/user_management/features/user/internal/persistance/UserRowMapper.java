package tech.kood.match_me.user_management.features.user.internal.persistance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.user.internal.persistance.userEntity.UserEntity;
import tech.kood.match_me.user_management.features.user.internal.persistance.userEntity.UserEntityFactory;


@Component
public class UserRowMapper implements RowMapper<UserEntity> {

    private final UserEntityFactory userEntityFactory;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserRowMapper.class);

    public UserRowMapper(UserEntityFactory userEntityFactory) {
        this.userEntityFactory = userEntityFactory;
    }

    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID id = rs.getObject("id", UUID.class);
        String email = rs.getString("email");
        String password_hash = rs.getString("password_hash");
        String password_salt = rs.getString("password_salt");
        Instant created_at = rs.getTimestamp("created_at").toInstant().truncatedTo(ChronoUnit.SECONDS);
        Instant updated_at = rs.getTimestamp("updated_at").toInstant().truncatedTo(ChronoUnit.SECONDS);

        try {
            return userEntityFactory.make(id, email, password_hash, password_salt, created_at,
                    updated_at);
        } catch (CheckedConstraintViolationException e) {
            logger.error("Failed to map row to userId entity: {}", e.getMessage());
            return null;
        }
    }
}
