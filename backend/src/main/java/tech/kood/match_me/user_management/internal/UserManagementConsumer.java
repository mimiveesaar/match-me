package tech.kood.match_me.user_management.internal;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import tech.kood.match_me.user_management.internal.features.getUser.handlers.GetUserByIdHandler;
import tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.api.CreateAccessTokenHandler;
import tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.api.CreateAccessTokenRequest;
import tech.kood.match_me.user_management.features.accessToken.features.validateAccessToken.api.ValidateAccessTokenHandler;
import tech.kood.match_me.user_management.features.accessToken.features.validateAccessToken.api.ValidateAccessTokenRequest;
import tech.kood.match_me.user_management.internal.features.login.LoginHandler;
import tech.kood.match_me.user_management.internal.features.login.LoginRequest;
import tech.kood.match_me.user_management.features.refreshToken.features.createToken.api.CreateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.getToken.GetRefreshTokenHandler;
import tech.kood.match_me.user_management.features.refreshToken.features.invalidateToken.api.InvalidateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.features.invalidateToken.api.InvalidateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.features.user.features.registerUser.api.RegisterUserRequest;

@Component
public class UserManagementConsumer {

    private final ObjectMapper objectMapper;

    private final CreateAccessTokenHandler getAccessTokenHandler;

    private final ValidateAccessTokenHandler validateAccessTokenHandler;

    private final GetUserByIdHandler getUserByIdHandler;

    private final GetUserByUsernameHandler getUserByUsernameHandler;

    private final GetUserByEmailHandler getUserByEmailHandler;

    private final LoginHandler loginHandler;

    private final CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler;

    private final GetRefreshTokenHandler getRefreshTokenHandler;

    private final InvalidateRefreshTokenCommandHandler invalidateRefreshTokenHandler;

    private final RegisterUserCommandHandler registerUserHandler;

    public UserManagementConsumer(CreateAccessTokenHandler getAccessTokenHandler,
                                  ValidateAccessTokenHandler validateAccessTokenHandler,
                                  GetUserByIdHandler getUserByIdHandler,
                                  GetUserByUsernameHandler getUserByUsernameHandler,
                                  GetUserByEmailHandler getUserByEmailHandler, LoginHandler loginHandler,
                                  CreateRefreshTokenCommandHandler createRefreshTokenCommandHandler,
                                  GetRefreshTokenHandler getRefreshTokenHandler,
                                  InvalidateRefreshTokenCommandHandler invalidateRefreshTokenHandler,
                                  RegisterUserCommandHandler registerUserHandler, ObjectMapper objectMapper) {
        this.getAccessTokenHandler = getAccessTokenHandler;
        this.validateAccessTokenHandler = validateAccessTokenHandler;
        this.getUserByIdHandler = getUserByIdHandler;
        this.getUserByUsernameHandler = getUserByUsernameHandler;
        this.getUserByEmailHandler = getUserByEmailHandler;
        this.loginHandler = loginHandler;
        this.createRefreshTokenCommandHandler = createRefreshTokenCommandHandler;
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
