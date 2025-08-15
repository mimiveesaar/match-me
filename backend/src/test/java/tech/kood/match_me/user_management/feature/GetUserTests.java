package tech.kood.match_me.user_management.feature;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.TestInstance;

import tech.kood.match_me.user_management.common.UserManagementTestBase;
import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.getUser.handlers.GetUserByEmailHandler;
import tech.kood.match_me.user_management.internal.features.getUser.handlers.GetUserByIdHandler;
import tech.kood.match_me.user_management.internal.features.getUser.handlers.GetUserByUsernameHandler;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByEmailQuery;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByIdQuery;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByUsernameQuery;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByEmailQueryResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByIdQueryResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByUsernameQueryResults;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;
import tech.kood.match_me.user_management.internal.features.user.domain.model.userId.UserId;
import tech.kood.match_me.user_management.mocks.RegisterUserRequestMocker;
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
        GetUserByEmailHandler getUserByEmailHandler;

        @Autowired
        GetUserByIdHandler getUserByIdHandler;

        @Autowired
        GetUserByUsernameHandler getUserByUsernameHandler;

        @Autowired
        RegisterUserRequestMocker registerUserRequestMocker;

        @Test
        void shouldGetUserByUsername() {
                var registerRequest = registerUserRequestMocker.createValidRequest();
                var registerResult = registerUserHandler.handle(registerRequest);
                assert registerResult instanceof RegisterUserResults.Success;

                var username = registerRequest.getUsername();

                var getRequest = GetUserByUsernameQuery.of(UUID.randomUUID(), username,
                                UUID.randomUUID().toString());

                var getResult = getUserByUsernameHandler.handle(getRequest);
                assert getResult instanceof GetUserByUsernameQueryResults.Success;
        }

        @Test
        void shouldGetUserById() {
                var registerRequest = registerUserRequestMocker.createValidRequest();
                var registerResult = registerUserHandler.handle(registerRequest);
                assert registerResult instanceof RegisterUserResults.Success;

                var userId = ((RegisterUserResults.Success) registerResult).getUser().getId();

                var getRequest = GetUserByIdQuery.of(UUID.randomUUID(), userId,
                                UUID.randomUUID().toString());

                var getResult = getUserByIdHandler.handle(getRequest);
                assert getResult instanceof GetUserByIdQueryResults.Success;
        }

        @Test
        void shouldGetUserByEmail() {
                var registerRequest = registerUserRequestMocker.createValidRequest();
                var registerResult = registerUserHandler.handle(registerRequest);
                assert registerResult instanceof RegisterUserResults.Success;

                var email = registerRequest.getEmail();

                var getRequest = GetUserByEmailQuery.of(UUID.randomUUID(), email,
                                UUID.randomUUID().toString());

                var getResult = getUserByEmailHandler.handle(getRequest);
                assert getResult instanceof GetUserByEmailQueryResults.Success;
        }

        @Test
        void shouldReturnUserNotFoundForInvalidUsername() {
                var getRequest = GetUserByUsernameQuery.of(UUID.randomUUID(), "nonexistentuser",
                                UUID.randomUUID().toString());

                var getResult = getUserByUsernameHandler.handle(getRequest);
                assert getResult instanceof GetUserByUsernameQueryResults.UserNotFound;
        }

        @Test
        void shouldReturnUserNotFoundForInvalidId() {
                var getRequest = GetUserByIdQuery.of(UUID.randomUUID(),
                                UserId.fromString(UUID.randomUUID().toString()),
                                UUID.randomUUID().toString());
                var getResult = getUserByIdHandler.handle(getRequest);
                assert getResult instanceof GetUserByIdQueryResults.UserNotFound;
        }

        @Test
        void shouldReturnUserNotFoundForInvalidEmail() {
                var getRequest = GetUserByEmailQuery.of(UUID.randomUUID(), "nonexistent@email.com",
                                UUID.randomUUID().toString());
                var getResult = getUserByEmailHandler.handle(getRequest);
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
                var username1 = req1.getUsername();
                var username2 = req2.getUsername();
                var getRequest1 = GetUserByUsernameQuery.of(UUID.randomUUID(), username1,
                                UUID.randomUUID().toString());
                var getRequest2 = GetUserByUsernameQuery.of(UUID.randomUUID(), username2,
                                UUID.randomUUID().toString());
                var getResult1 = getUserByUsernameHandler.handle(getRequest1);
                var getResult2 = getUserByUsernameHandler.handle(getRequest2);
                assert getResult1 instanceof GetUserByUsernameQueryResults.Success;
                assert getResult2 instanceof GetUserByUsernameQueryResults.Success;
        }

        @Test
        void shouldGetUserByEmailAfterRegisteringMultipleUsers() {
                var req1 = registerUserRequestMocker.createValidRequest();
                var req2 = registerUserRequestMocker.createValidRequest();
                var res1 = registerUserHandler.handle(req1);
                var res2 = registerUserHandler.handle(req2);
                assert res1 instanceof RegisterUserResults.Success;
                assert res2 instanceof RegisterUserResults.Success;
                var email1 = req1.getEmail();
                var email2 = req2.getEmail();
                var getRequest1 = GetUserByEmailQuery.of(UUID.randomUUID(), email1,
                                UUID.randomUUID().toString());
                var getRequest2 = GetUserByEmailQuery.of(UUID.randomUUID(), email2,
                                UUID.randomUUID().toString());
                var getResult1 = getUserByEmailHandler.handle(getRequest1);
                var getResult2 = getUserByEmailHandler.handle(getRequest2);
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
                var userId1 = ((RegisterUserResults.Success) res1).getUser().getId();
                var userId2 = ((RegisterUserResults.Success) res2).getUser().getId();
                var getRequest1 = GetUserByIdQuery.of(UUID.randomUUID(), userId1,
                                UUID.randomUUID().toString());
                var getRequest2 = GetUserByIdQuery.of(UUID.randomUUID(), userId2,
                                UUID.randomUUID().toString());
                var getResult1 = getUserByIdHandler.handle(getRequest1);
                var getResult2 = getUserByIdHandler.handle(getRequest2);
                assert getResult1 instanceof GetUserByIdQueryResults.Success;
                assert getResult2 instanceof GetUserByIdQueryResults.Success;
        }

}
