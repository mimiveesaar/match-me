package tech.kood.match_me.user_management.feature;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.getUser.GetUserHandler;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByUsernameRequest;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByUsernameResults;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class GetUserTests extends UserManagementTestBase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("userManagementFlyway") Flyway userManagementFlyway;

    @Autowired
    RegisterUserHandler registerUserHandler;

    @Autowired
    GetUserHandler getUserHandler;

    @BeforeEach
    void setUp() {
        userManagementFlyway.migrate();
        userRepository.deleteAll();
    }

    @Test
    void shouldGetUserByUsername() {
        var registerRequest = RegisterUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUserResults.Success;

        var username = registerRequest.username();

        var getRequest = new GetUserByUsernameRequest(
            UUID.randomUUID(),
            username,
            Optional.of(UUID.randomUUID().toString())
        );

        var getResult = getUserHandler.handle(getRequest);
        assert getResult instanceof GetUserByUsernameResults.Success;
    }
}