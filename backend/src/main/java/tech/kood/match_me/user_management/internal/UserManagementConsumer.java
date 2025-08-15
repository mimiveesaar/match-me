package tech.kood.match_me.user_management.internal;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import tech.kood.match_me.user_management.internal.features.getUser.handlers.GetUserByEmailHandler;
import tech.kood.match_me.user_management.internal.features.getUser.handlers.GetUserByIdHandler;
import tech.kood.match_me.user_management.internal.features.getUser.handlers.GetUserByUsernameHandler;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByEmailQuery;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByIdQuery;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByUsernameQuery;
import tech.kood.match_me.user_management.internal.features.jwt.createAccessToken.CreateAccessTokenHandler;
import tech.kood.match_me.user_management.internal.features.jwt.createAccessToken.CreateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenHandler;
import tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken.ValidateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.features.login.LoginHandler;
import tech.kood.match_me.user_management.internal.features.login.LoginRequest;
import tech.kood.match_me.user_management.internal.features.refreshToken.createToken.CreateRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.getToken.GetRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken.InvalidateRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken.InvalidateRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.features.user.registerUser.RegisterUserHandler;
import tech.kood.match_me.user_management.internal.features.user.registerUser.RegisterUserRequest;

@Component
public class UserManagementConsumer {

    private final ObjectMapper objectMapper;

    private final CreateAccessTokenHandler getAccessTokenHandler;

    private final ValidateAccessTokenHandler validateAccessTokenHandler;

    private final GetUserByIdHandler getUserByIdHandler;

    private final GetUserByUsernameHandler getUserByUsernameHandler;

    private final GetUserByEmailHandler getUserByEmailHandler;

    private final LoginHandler loginHandler;

    private final CreateRefreshTokenHandler createRefreshTokenHandler;

    private final GetRefreshTokenHandler getRefreshTokenHandler;

    private final InvalidateRefreshTokenHandler invalidateRefreshTokenHandler;

    private final RegisterUserHandler registerUserHandler;

    public UserManagementConsumer(CreateAccessTokenHandler getAccessTokenHandler,
            ValidateAccessTokenHandler validateAccessTokenHandler,
            GetUserByIdHandler getUserByIdHandler,
            GetUserByUsernameHandler getUserByUsernameHandler,
            GetUserByEmailHandler getUserByEmailHandler, LoginHandler loginHandler,
            CreateRefreshTokenHandler createRefreshTokenHandler,
            GetRefreshTokenHandler getRefreshTokenHandler,
            InvalidateRefreshTokenHandler invalidateRefreshTokenHandler,
            RegisterUserHandler registerUserHandler, ObjectMapper objectMapper) {
        this.getAccessTokenHandler = getAccessTokenHandler;
        this.validateAccessTokenHandler = validateAccessTokenHandler;
        this.getUserByIdHandler = getUserByIdHandler;
        this.getUserByUsernameHandler = getUserByUsernameHandler;
        this.getUserByEmailHandler = getUserByEmailHandler;
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

                if (type.equals(CreateAccessTokenRequest.class.getName())) {
                    CreateAccessTokenRequest request =
                            objectMapper.readValue(payload, CreateAccessTokenRequest.class);

                    return getAccessTokenHandler.handle(request);
                } else if (type.equals(ValidateAccessTokenRequest.class.getName())) {
                    ValidateAccessTokenRequest request =
                            objectMapper.readValue(payload, ValidateAccessTokenRequest.class);

                    return validateAccessTokenHandler.handle(request);
                } else if (type.equals(GetUserByEmailQuery.class.getName())) {
                    GetUserByEmailQuery request =
                            objectMapper.readValue(payload, GetUserByEmailQuery.class);

                    return getUserByEmailHandler.handle(request);
                } else if (type.equals(GetUserByIdQuery.class.getName())) {
                    GetUserByIdQuery request =
                            objectMapper.readValue(payload, GetUserByIdQuery.class);

                    return getUserByIdHandler.handle(request);
                } else if (type.equals(GetUserByUsernameQuery.class.getName())) {
                    GetUserByUsernameQuery request =
                            objectMapper.readValue(payload, GetUserByUsernameQuery.class);

                    return getUserByUsernameHandler.handle(request);
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
