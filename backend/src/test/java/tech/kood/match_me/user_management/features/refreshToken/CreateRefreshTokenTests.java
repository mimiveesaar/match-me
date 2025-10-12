package tech.kood.match_me.user_management.features.refreshToken;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.features.refreshToken.actions.CreateRefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;
import tech.kood.match_me.user_management.features.user.actions.GetUserById;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;
import tech.kood.match_me.user_management.features.user.actions.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@Transactional(transactionManager = "userManagementTransactionManager")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateRefreshTokenTests extends UserManagementTestBase {

    @Autowired
    RegisterUser.Handler registerUserHandler;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    CreateRefreshToken.Handler createRefreshTokenCommandHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;

    @Autowired
    GetUserById.Handler getUserById;

    @Test
    void shouldCreateRefreshTokenForValidUser() throws CheckedConstraintViolationException {
        var registerRequest = registerUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUser.Result.Success;

        var userId = ((RegisterUser.Result.Success) registerResult).userId();

        var getUserResult = getUserById.handle(new GetUserById.Request(userId));

        assert getUserResult instanceof GetUserById.Result.Success;
        var user = ((GetUserById.Result.Success) getUserResult).user();

        var createTokenRequest = new CreateRefreshToken.Request(user.id());
        var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);
        assert createTokenResult instanceof CreateRefreshToken.Result.Success;

        var token = refreshTokenRepository.findToken(((CreateRefreshToken.Result.Success) createTokenResult).refreshToken().secret().toString());
        assert token.isPresent() : "The refresh token should be created and found in the repository";
        assert token.get().getUserId().equals(user.id().value()) : "The refresh token should belong to the correct userId";
    }

    @Test
    void shouldHandleUserNotFound() {

        // Create a request with a non-existent userId ID to simulate userId not found
        var createTokenRequest = new CreateRefreshToken.Request(new UserIdDTO(UUID.randomUUID()));
        var result = createRefreshTokenCommandHandler.handle(createTokenRequest);


        assert result instanceof CreateRefreshToken.Result.UserNotFound : "The handler should return a Success result for valid userId";
    }
}