package tech.kood.match_me.user_management.internal.database.repostitories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class RefreshTokenRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    public RefreshTokenRepository(
        @Qualifier("userManagementNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }
}
