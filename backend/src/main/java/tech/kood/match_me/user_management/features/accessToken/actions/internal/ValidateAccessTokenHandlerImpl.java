package tech.kood.match_me.user_management.features.accessToken.actions.internal;

import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.common.domain.internal.accessToken.AccessTokenFactory;
import tech.kood.match_me.user_management.features.accessToken.actions.ValidateAccessToken;
import tech.kood.match_me.user_management.features.accessToken.internal.mapper.AccessTokenMapper;

import java.util.UUID;

@Service
public class ValidateAccessTokenHandlerImpl implements ValidateAccessToken.Handler {

    private static final Logger logger = LoggerFactory.getLogger(ValidateAccessTokenHandlerImpl.class);
    private final JWTVerifier jwtVerifier;
    private final AccessTokenFactory accessTokenFactory;
    private final AccessTokenMapper accessTokenMapper;
    private final UserIdFactory userIdFactory;
    private final Validator validator;


    public ValidateAccessTokenHandlerImpl(
            @Qualifier("userManagementJwtVerifier") JWTVerifier jwtVerifier,
            AccessTokenFactory accessTokenFactory,
            AccessTokenMapper accessTokenMapper,
            UserIdFactory userIdFactory,
            Validator validator) {
        this.jwtVerifier = jwtVerifier;
        this.accessTokenFactory = accessTokenFactory;
        this.accessTokenMapper = accessTokenMapper;
        this.userIdFactory = userIdFactory;
        this.validator = validator;
    }

    @Override
    @Transactional
    public ValidateAccessToken.Result handle(ValidateAccessToken.Request request) {

        var validationResults = validator.validate(request);
        if (!validationResults.isEmpty()) {
            return new ValidateAccessToken.Result.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            var decodedJWT = jwtVerifier.verify(request.jwtToken());
            Claim userIdClaim = decodedJWT.getClaim("userId");

            if (userIdClaim.isMissing()) {
                throw new IllegalArgumentException("Invalid JWT: userId claim is missing");
            }

            var userId = userIdFactory.create(UUID.fromString(userIdClaim.asString()));
            var userIdDto = new UserIdDTO(userId.getValue());
            var accessToken = accessTokenFactory.create(request.jwtToken(), userId);
            var accessTokenDTO = accessTokenMapper.toDTO(accessToken);

            return new ValidateAccessToken.Result.Success(accessTokenDTO, userIdDto);

        } catch (JWTVerificationException | IllegalArgumentException e) {
            logger.warn("Invalid JWT: {}", e.getMessage());
            return new ValidateAccessToken.Result.InvalidToken();
        } catch (CheckedConstraintViolationException e) {
            logger.error("Invalid signed JWT: {}", e.getMessage());
            return new ValidateAccessToken.Result.SystemError(e.getMessage());
        }
    }
}
