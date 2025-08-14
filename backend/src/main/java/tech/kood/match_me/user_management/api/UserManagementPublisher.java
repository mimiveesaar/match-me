package tech.kood.match_me.user_management.api;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.internal.UserManagementMessaging;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByEmailQuery;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByIdQuery;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByUsernameQuery;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByEmailQueryResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByIdQueryResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByUsernameQueryResults;
import tech.kood.match_me.user_management.internal.features.jwt.createAccessToken.CreateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.features.jwt.createAccessToken.CreateAccessTokenResults;
import tech.kood.match_me.user_management.internal.features.login.LoginRequest;
import tech.kood.match_me.user_management.internal.features.login.LoginResults;
import tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken.InvalidateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken.InvalidateRefreshTokenResults;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserRequest;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;

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
