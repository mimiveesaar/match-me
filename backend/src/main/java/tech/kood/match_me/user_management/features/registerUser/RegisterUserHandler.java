package tech.kood.match_me.user_management.features;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserHandler {

    private final JdbcTemplate jdbc;

    public RegisterUserHandler(@Qualifier("userManagementJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

}
