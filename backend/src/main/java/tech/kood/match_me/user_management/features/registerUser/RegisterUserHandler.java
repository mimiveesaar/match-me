package tech.kood.match_me.user_management.features.registerUser;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserHandler {


    private final NamedParameterJdbcTemplate jdbcTemplate;
    public RegisterUserHandler(@Qualifier("userManagementNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    



}
