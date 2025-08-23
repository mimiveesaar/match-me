package tech.kood.match_me.user_management.features.refreshToken;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.refreshToken.internal.persistance.RefreshTokenRepository;
import tech.kood.match_me.user_management.features.user.features.getUser.api.GetUserByIdQueryHandler;
import tech.kood.match_me.user_management.features.user.features.getUser.api.GetUserByIdRequest;
import tech.kood.match_me.user_management.features.user.features.getUser.api.GetUserByIdResults;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserResults;
import tech.kood.match_me.user_management.features.user.features.registerUser.RegisterUserRequestMocker;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateRefreshTokenTests extends UserManagementTestBase {

    @Autowired
    RegisterUserCommandHandler registerUserHandler;

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
        assert registerResult instanceof RegisterUserResults.Success;

        var userId = ((RegisterUserResults.Success) registerResult).userId();

        var getUserResult = getUserByIdQueryHandler.handle(new GetUserByIdRequest(userId, null));

        assert getUserResult instanceof GetUserByIdResults.Success;
        var user = ((GetUserByIdResults.Success) getUserResult).user();

        var createTokenRequest = new CreateRefreshTokenRequest(user.id(), null);
        var createTokenResult = createRefreshTokenCommandHandler.handle(createTokenRequest);
        assert createTokenResult instanceof CreateRefreshTokenResults.Success;

        var token = refreshTokenRepository.findToken(((CreateRefreshTokenResults.Success) createTokenResult).refreshToken().secret().toString());
        assert token.isPresent() : "The refresh token should be created and found in the repository";
        assert token.get().getUserId().equals(user.id().value()) : "The refresh token should belong to the correct userId";
    }

    @Test
    void shouldHandleUserNotFound() {

        // Create a request with a non-existent userId ID to simulate userId not found
        var createTokenRequest = new CreateRefreshTokenRequest(new UserIdDTO(UUID.randomUUID()),null);
        var result = createRefreshTokenCommandHandler.handle(createTokenRequest);


        assert result instanceof CreateRefreshTokenResults.UserNotFound : "The handler should return a Success result for valid userId";
    }
}
