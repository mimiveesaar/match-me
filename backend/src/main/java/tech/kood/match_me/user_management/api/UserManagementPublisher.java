package tech.kood.match_me.user_management.api;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.internal.UserManagementMessaging;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByEmailQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByIdQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByUsernameQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByEmailQueryResults;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByIdQueryResults;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByUsernameQueryResults;
import tech.kood.match_me.user_management.internal.domain.features.jwt.createAccessToken.CreateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.domain.features.jwt.createAccessToken.CreateAccessTokenResults;
import tech.kood.match_me.user_management.internal.domain.features.jwt.validateAccessToken.ValidateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.domain.features.jwt.validateAccessToken.ValidateAccessTokenResults;
import tech.kood.match_me.user_management.internal.domain.features.login.LoginRequest;
import tech.kood.match_me.user_management.internal.domain.features.login.LoginResults;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.createToken.CreateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.createToken.CreateRefreshTokenResults;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.getToken.GetRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.getToken.GetRefreshTokenResults;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.invalidateToken.InvalidateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.invalidateToken.InvalidateRefreshTokenResults;
import tech.kood.match_me.user_management.internal.domain.features.registerUser.RegisterUserRequest;
import tech.kood.match_me.user_management.internal.domain.features.registerUser.RegisterUserResults;

@Component
public class UserManagementPublisher {
    private final JmsMessagingTemplate jmsMessaging;

    public UserManagementPublisher(JmsMessagingTemplate jmsMessaging) {
        this.jmsMessaging = jmsMessaging;
    }

    public CreateAccessTokenResults publish(CreateAccessTokenRequest request) {
        return jmsMessaging.convertSendAndReceive(UserManagementMessaging.USER_MANAGEMENT_QUEUE,
                request, CreateAccessTokenResults.class);
    }


    public GetUserByIdQueryResults publish(GetUserByIdQuery request) {
        return jmsMessaging.convertSendAndReceive(UserManagementMessaging.USER_MANAGEMENT_QUEUE,
                request, GetUserByIdQueryResults.class);
    }

    public GetUserByUsernameQueryResults publish(GetUserByUsernameQuery request) {
        return jmsMessaging.convertSendAndReceive(UserManagementMessaging.USER_MANAGEMENT_QUEUE,
                request, GetUserByUsernameQueryResults.class);
    }

    public GetUserByEmailQueryResults publish(GetUserByEmailQuery request) {
        return jmsMessaging.convertSendAndReceive(UserManagementMessaging.USER_MANAGEMENT_QUEUE,
                request, GetUserByEmailQueryResults.class);
    }

    public LoginResults publish(LoginRequest request) {
        return jmsMessaging.convertSendAndReceive(UserManagementMessaging.USER_MANAGEMENT_QUEUE,
                request, LoginResults.class);
    }

    public InvalidateRefreshTokenResults publish(InvalidateRefreshTokenRequest request) {
        return jmsMessaging.convertSendAndReceive(UserManagementMessaging.USER_MANAGEMENT_QUEUE,
                request, InvalidateRefreshTokenResults.class);
    }

    public RegisterUserResults publish(RegisterUserRequest request) {
        return jmsMessaging.convertSendAndReceive(UserManagementMessaging.USER_MANAGEMENT_QUEUE,
                request, RegisterUserResults.class);
    }
}
