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
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;
import tech.kood.match_me.user_management.features.user.actions.getUser.api.GetUserByIdQueryHandler;
import tech.kood.match_me.user_management.features.user.actions.getUser.api.GetUserByIdRequest;
import tech.kood.match_me.user_management.features.user.actions.getUser.api.GetUserByIdResults;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.actions.createToken.api.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.features.user.actions.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateRefreshTokenTests extends UserManagementTestBase {

    @Autowired
    RegisterUser.Handler registerUserHandler;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler;

    @Autowired
    RegisterUserRequestMocker registerUserRequestMocker;

    @Autowired
    GetUserByIdQueryHandler getUserByIdQueryHandler;


    @Test
    void shouldCreateRefreshTokenForValidUser() throws CheckedConstraintViolationException {
        var registerRequest = registerUserRequestMocker.createValidRequest();
        var registerResult = registerUserHandler.handle(registerRequest);
        assert registerResult instanceof RegisterUser.Result.Success;

        var userId = ((RegisterUser.Result.Success) registerResult).userId();

        var getUserResult = getUserByIdQueryHandler.handle(new GetUserByIdRequest(userId));

        assert getUserResult instanceof GetUserByIdResults.Success;
        var user = ((GetUserByIdResults.Success) getUserResult).user();

        var createTokenRequest = new CreateRefreshTokenRequest(user.id());
        var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);
        assert createTokenResult instanceof CreateRefreshTokenResults.Success;

        var token = refreshTokenRepository.findToken(((CreateRefreshTokenResults.Success) createTokenResult).refreshToken().secret().toString());
        assert token.isPresent() : "The refresh token should be created and found in the repository";
        assert token.get().getUserId().equals(user.id().value()) : "The refresh token should belong to the correct userId";
    }

    @Test
    void shouldHandleUserNotFound() {

        // Create a request with a non-existent userId ID to simulate userId not found
        var createTokenRequest = new CreateRefreshTokenRequest(new UserIdDTO(UUID.randomUUID()));
        var result = createRefreshTokenCommandHandler.handle(createTokenRequest);


        assert result instanceof CreateRefreshTokenResults.UserNotFound : "The handler should return a Success result for valid userId";
    }
}
