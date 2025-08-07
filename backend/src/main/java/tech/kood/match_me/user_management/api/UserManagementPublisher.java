package tech.kood.match_me.user_management.api;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.internal.UserManagementMessaging;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByEmailQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByIdQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByUsernameRequest;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByEmailResults;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByIdResults;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByUsernameResults;
import tech.kood.match_me.user_management.internal.domain.features.jwt.getAccessToken.GetAccessTokenRequest;
import tech.kood.match_me.user_management.internal.domain.features.jwt.getAccessToken.GetAccessTokenResults;
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

    public GetAccessTokenResults publish(GetAccessTokenRequest request) {
        return jmsMessaging.convertSendAndReceive(UserManagementMessaging.USER_MANAGEMENT_QUEUE,
                request, GetAccessTokenResults.class);
    }


    public GetUserByIdResults publish(GetUserByIdQuery request) {
        return jmsMessaging.convertSendAndReceive(UserManagementMessaging.USER_MANAGEMENT_QUEUE,
                request, GetUserByIdResults.class);
    }

    public GetUserByUsernameResults publish(GetUserByUsernameRequest request) {
        return jmsMessaging.convertSendAndReceive(UserManagementMessaging.USER_MANAGEMENT_QUEUE,
                request, GetUserByUsernameResults.class);
    }

    public GetUserByEmailResults publish(GetUserByEmailQuery request) {
        return jmsMessaging.convertSendAndReceive(UserManagementMessaging.USER_MANAGEMENT_QUEUE,
                request, GetUserByEmailResults.class);
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
