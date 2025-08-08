package tech.kood.match_me.user_management.feature;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.TestInstance;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.domain.features.getUser.GetUserHandler;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByEmailQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByIdQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByUsernameQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByEmailQueryResults;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByIdQueryResults;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByUsernameResults;
import tech.kood.match_me.user_management.internal.domain.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.internal.domain.features.registerUser.RegisterUserResults;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class GetUserTests extends UserManagementTestBase {

        @Autowired
        UserRepository userRepository;

        @Autowired
        @Qualifier("userManagementFlyway")
        Flyway userManagementFlyway;

        @Autowired
        RegisterUserHandler registerUserHandler;

        @Autowired
        GetUserHandler getUserHandler;

        @Autowired
        RegisterUserRequestMocker registerUserRequestMocker;

        @Test
        void shouldGetUserByUsername() {
                var registerRequest = registerUserRequestMocker.createValidRequest();
                var registerResult = registerUserHandler.handle(registerRequest);
                assert registerResult instanceof RegisterUserResults.Success;

                var username = registerRequest.username();

                var getRequest = new GetUserByUsernameQuery(UUID.randomUUID().toString(), username,
                                UUID.randomUUID().toString());

                var getResult = getUserHandler.handle(getRequest);
                assert getResult instanceof GetUserByUsernameResults.Success;
        }

        @Test
        void shouldGetUserById() {
                var registerRequest = registerUserRequestMocker.createValidRequest();
                var registerResult = registerUserHandler.handle(registerRequest);
                assert registerResult instanceof RegisterUserResults.Success;

                var userId = ((RegisterUserResults.Success) registerResult).user().id();

                var getRequest = new GetUserByIdQuery(UUID.randomUUID().toString(),
                                userId.toString(), UUID.randomUUID().toString());

                var getResult = getUserHandler.handle(getRequest);
                assert getResult instanceof GetUserByIdQueryResults.Success;
        }

        @Test
        void shouldGetUserByEmail() {
                var registerRequest = registerUserRequestMocker.createValidRequest();
                var registerResult = registerUserHandler.handle(registerRequest);
                assert registerResult instanceof RegisterUserResults.Success;

                var email = registerRequest.email();

                var getRequest = new GetUserByEmailQuery(UUID.randomUUID().toString(), email,
                                UUID.randomUUID().toString());

                var getResult = getUserHandler.handle(getRequest);
                assert getResult instanceof GetUserByEmailQueryResults.Success;
        }

        @Test
        void shouldReturnUserNotFoundForInvalidUsername() {
                var getRequest = new GetUserByUsernameQuery(UUID.randomUUID().toString(),
                                "nonexistentuser", UUID.randomUUID().toString());

                var getResult = getUserHandler.handle(getRequest);
                assert getResult instanceof GetUserByUsernameResults.UserNotFound;
        }

        @Test
        void shouldReturnUserNotFoundForInvalidId() {
                var getRequest = new GetUserByIdQuery(UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(), UUID.randomUUID().toString());
                var getResult = getUserHandler.handle(getRequest);
                assert getResult instanceof GetUserByIdQueryResults.UserNotFound;
        }

        @Test
        void shouldReturnUserNotFoundForInvalidEmail() {
                var getRequest = new GetUserByEmailQuery(UUID.randomUUID().toString(),
                                "nonexistent@email.com", UUID.randomUUID().toString());
                var getResult = getUserHandler.handle(getRequest);
                assert getResult instanceof GetUserByEmailQueryResults.UserNotFound;
        }

        @Test
        void shouldGetUserByUsernameAfterRegisteringMultipleUsers() {
                var req1 = registerUserRequestMocker.createValidRequest();
                var req2 = registerUserRequestMocker.createValidRequest();
                var res1 = registerUserHandler.handle(req1);
                var res2 = registerUserHandler.handle(req2);
                assert res1 instanceof RegisterUserResults.Success;
                assert res2 instanceof RegisterUserResults.Success;
                var username1 = req1.username();
                var username2 = req2.username();
                var getRequest1 = new GetUserByUsernameQuery(UUID.randomUUID().toString(),
                                username1, UUID.randomUUID().toString());
                var getRequest2 = new GetUserByUsernameQuery(UUID.randomUUID().toString(),
                                username2, UUID.randomUUID().toString());
                var getResult1 = getUserHandler.handle(getRequest1);
                var getResult2 = getUserHandler.handle(getRequest2);
                assert getResult1 instanceof GetUserByUsernameResults.Success;
                assert getResult2 instanceof GetUserByUsernameResults.Success;
        }

        @Test
        void shouldGetUserByEmailAfterRegisteringMultipleUsers() {
                var req1 = registerUserRequestMocker.createValidRequest();
                var req2 = registerUserRequestMocker.createValidRequest();
                var res1 = registerUserHandler.handle(req1);
                var res2 = registerUserHandler.handle(req2);
                assert res1 instanceof RegisterUserResults.Success;
                assert res2 instanceof RegisterUserResults.Success;
                var email1 = req1.email();
                var email2 = req2.email();
                var getRequest1 = new GetUserByEmailQuery(UUID.randomUUID().toString(), email1,
                                UUID.randomUUID().toString());
                var getRequest2 = new GetUserByEmailQuery(UUID.randomUUID().toString(), email2,
                                UUID.randomUUID().toString());
                var getResult1 = getUserHandler.handle(getRequest1);
                var getResult2 = getUserHandler.handle(getRequest2);
                assert getResult1 instanceof GetUserByEmailQueryResults.Success;
                assert getResult2 instanceof GetUserByEmailQueryResults.Success;
        }

        @Test
        void shouldGetUserByIdAfterRegisteringMultipleUsers() {
                var req1 = registerUserRequestMocker.createValidRequest();
                var req2 = registerUserRequestMocker.createValidRequest();
                var res1 = registerUserHandler.handle(req1);
                var res2 = registerUserHandler.handle(req2);
                assert res1 instanceof RegisterUserResults.Success;
                assert res2 instanceof RegisterUserResults.Success;
                var userId1 = ((RegisterUserResults.Success) res1).user().id();
                var userId2 = ((RegisterUserResults.Success) res2).user().id();
                var getRequest1 = new GetUserByIdQuery(UUID.randomUUID().toString(),
                                userId1.toString(), UUID.randomUUID().toString());
                var getRequest2 = new GetUserByIdQuery(UUID.randomUUID().toString(),
                                userId2.toString(), UUID.randomUUID().toString());
                var getResult1 = getUserHandler.handle(getRequest1);
                var getResult2 = getUserHandler.handle(getRequest2);
                assert getResult1 instanceof GetUserByIdQueryResults.Success;
                assert getResult2 instanceof GetUserByIdQueryResults.Success;
        }

}
