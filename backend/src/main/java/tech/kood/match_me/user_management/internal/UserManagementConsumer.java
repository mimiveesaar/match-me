package tech.kood.match_me.user_management.internal;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import tech.kood.match_me.user_management.internal.domain.features.getUser.GetUserHandler;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByEmailQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByIdQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByUsernameRequest;
import tech.kood.match_me.user_management.internal.domain.features.jwt.getAccessToken.GetAccessTokenHandler;
import tech.kood.match_me.user_management.internal.domain.features.jwt.getAccessToken.GetAccessTokenRequest;
import tech.kood.match_me.user_management.internal.domain.features.jwt.validateAccessToken.ValidateAccessTokenHandler;
import tech.kood.match_me.user_management.internal.domain.features.jwt.validateAccessToken.ValidateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.domain.features.login.LoginHandler;
import tech.kood.match_me.user_management.internal.domain.features.login.LoginRequest;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.createToken.CreateRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.getToken.GetRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.invalidateToken.InvalidateRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.invalidateToken.InvalidateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.domain.features.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.internal.domain.features.registerUser.RegisterUserRequest;

@Component
public class UserManagementConsumer {

    private final ObjectMapper objectMapper;

    private final GetAccessTokenHandler getAccessTokenHandler;

    private final ValidateAccessTokenHandler validateAccessTokenHandler;

    private final GetUserHandler getUserHandler;

    private final LoginHandler loginHandler;

    private final CreateRefreshTokenHandler createRefreshTokenHandler;

    private final GetRefreshTokenHandler getRefreshTokenHandler;

    private final InvalidateRefreshTokenHandler invalidateRefreshTokenHandler;

    private final RegisterUserHandler registerUserHandler;

    public UserManagementConsumer(GetAccessTokenHandler getAccessTokenHandler,
            ValidateAccessTokenHandler validateAccessTokenHandler, GetUserHandler getUserHandler,
            LoginHandler loginHandler, CreateRefreshTokenHandler createRefreshTokenHandler,
            GetRefreshTokenHandler getRefreshTokenHandler,
            InvalidateRefreshTokenHandler invalidateRefreshTokenHandler,
            RegisterUserHandler registerUserHandler, ObjectMapper objectMapper) {
        this.getAccessTokenHandler = getAccessTokenHandler;
        this.validateAccessTokenHandler = validateAccessTokenHandler;
        this.getUserHandler = getUserHandler;
        this.loginHandler = loginHandler;
        this.createRefreshTokenHandler = createRefreshTokenHandler;
        this.getRefreshTokenHandler = getRefreshTokenHandler;
        this.invalidateRefreshTokenHandler = invalidateRefreshTokenHandler;
        this.registerUserHandler = registerUserHandler;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "tech.kood.match_me.user_management.queue", concurrency = "1-1")
    public Object consume(Message message) {
        try {
            String type = message.getStringProperty("_type");

            if (type == null || type.isBlank()) {
                throw new JMSException("Message type is not set");
            }

            if (message instanceof TextMessage textMessage) {
                String payload = textMessage.getText();

                if (type.equals(GetAccessTokenRequest.class.getName())) {
                    GetAccessTokenRequest request =
                            objectMapper.readValue(payload, GetAccessTokenRequest.class);

                    return getAccessTokenHandler.handle(request);
                } else if (type.equals(ValidateAccessTokenRequest.class.getName())) {
                    ValidateAccessTokenRequest request =
                            objectMapper.readValue(payload, ValidateAccessTokenRequest.class);

                    return validateAccessTokenHandler.handle(request);
                } else if (type.equals(GetUserByEmailQuery.class.getName())) {
                    GetUserByEmailQuery request =
                            objectMapper.readValue(payload, GetUserByEmailQuery.class);

                    return getUserHandler.handle(request);
                } else if (type.equals(GetUserByIdQuery.class.getName())) {
                    GetUserByIdQuery request =
                            objectMapper.readValue(payload, GetUserByIdQuery.class);

                    return getUserHandler.handle(request);
                } else if (type.equals(GetUserByUsernameRequest.class.getName())) {
                    GetUserByUsernameRequest request =
                            objectMapper.readValue(payload, GetUserByUsernameRequest.class);

                    return getUserHandler.handle(request);
                } else if (type.equals(LoginRequest.class.getName())) {
                    LoginRequest request = objectMapper.readValue(payload, LoginRequest.class);

                    return loginHandler.handle(request);
                } else if (type.equals(InvalidateRefreshTokenRequest.class.getName())) {
                    InvalidateRefreshTokenRequest request =
                            objectMapper.readValue(payload, InvalidateRefreshTokenRequest.class);

                    return invalidateRefreshTokenHandler.handle(request);
                } else if (type.equals(RegisterUserRequest.class.getName())) {
                    RegisterUserRequest request =
                            objectMapper.readValue(payload, RegisterUserRequest.class);

                    return registerUserHandler.handle(request);
                } else {
                    return null;
                }

            }
        } catch (JMSException | JsonProcessingException e) {
            e.printStackTrace();
        } // This is used for polymorphic handling

        return message;
    }
}
