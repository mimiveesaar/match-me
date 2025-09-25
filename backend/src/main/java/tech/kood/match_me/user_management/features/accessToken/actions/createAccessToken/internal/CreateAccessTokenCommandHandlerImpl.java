package tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.internal;

import java.time.Instant;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import tech.kood.match_me.user_management.UserManagementConfig;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.user_management.features.accessToken.domain.internal.AccessTokenFactory;
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.AccessTokenCreatedEvent;
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.CreateAccessTokenCommandHandler;
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.CreateAccessTokenRequest;
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.CreateAccessTokenResults;
import tech.kood.match_me.user_management.features.accessToken.internal.mapper.AccessTokenMapper;
import tech.kood.match_me.user_management.features.refreshToken.actions.getToken.api.GetRefreshTokenQueryHandler;
import tech.kood.match_me.user_management.features.refreshToken.actions.getToken.api.GetRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.actions.getToken.api.GetRefreshTokenResults;

@Service
@ApplicationLayer
public class CreateAccessTokenCommandHandlerImpl implements CreateAccessTokenCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(CreateAccessTokenCommandHandlerImpl.class);
    private final ApplicationEventPublisher events;
    private final GetRefreshTokenQueryHandler getRefreshTokenHandler;
    private final UserManagementConfig userManagementConfig;
    private final AccessTokenFactory accessTokenFactory;
    private final AccessTokenMapper accessTokenMapper;
    private final UserIdFactory userIdFactory;
    private final Algorithm jwtAlgo;
    private final Validator validator;

    public CreateAccessTokenCommandHandlerImpl(ApplicationEventPublisher events,
                                               GetRefreshTokenQueryHandler getRefreshTokenHandler,
                                               UserManagementConfig userManagementConfig,
                                               AccessTokenFactory accessTokenFactory,
                                               AccessTokenMapper accessTokenMapper,
                                               UserIdFactory userIdFactory,
                                               @Qualifier("userManagementJwtAlgorithm") Algorithm jwtAlgo,
                                               Validator validator) {
        this.events = events;
        this.getRefreshTokenHandler = getRefreshTokenHandler;
        this.userManagementConfig = userManagementConfig;
        this.accessTokenFactory = accessTokenFactory;
        this.accessTokenMapper = accessTokenMapper;
        this.userIdFactory = userIdFactory;
        this.jwtAlgo = jwtAlgo;
        this.validator = validator;
    }

    @Transactional
    @Override
    public CreateAccessTokenResults handle(CreateAccessTokenRequest request) {
        try {

            var validationResults = validator.validate(request);
            if (!validationResults.isEmpty()) {
                return new CreateAccessTokenResults.InvalidRequest(InvalidInputErrorDTO.from(validationResults));
            }

            var refreshTokenRequest = new GetRefreshTokenRequest(request.secret());
            var refreshTokenResult = getRefreshTokenHandler.handle(refreshTokenRequest);

            if (refreshTokenResult instanceof GetRefreshTokenResults.InvalidRequest invalidRequest) {
                //This should never run. But for application correctness, we return an error.
                var error = new CreateAccessTokenResults.SystemError("Unexpected result from refresh token handler");
                logger.error(error.message());
                return error;
            }

            if (refreshTokenResult instanceof GetRefreshTokenResults.SystemError systemError) {
                //Something went wrong with the refresh token handler.
                //In a real-world application we should revert to retrying, but this is an alien dating website, so fuck that.
                //Return an error.

                logger.error(systemError.message());
                return new CreateAccessTokenResults.SystemError(systemError.message());

            }

            if (refreshTokenResult instanceof GetRefreshTokenResults.InvalidSecret invalidSecret) {
                // Return an error if the refresh token is invalid.
                return new CreateAccessTokenResults.InvalidToken();
            }


            if (refreshTokenResult instanceof GetRefreshTokenResults.Success refreshToken) {

                // Generate JWT token using the refresh token's userId ID.
                var issuedAt = Instant.now();
                var expiresAt = issuedAt.plusSeconds(userManagementConfig.getJwtExpiration());
                var userId = userIdFactory.create(refreshToken.refreshToken().userId().value());

                String jwt = JWT.create()
                        .withIssuer(userManagementConfig.getJwtIssuer())
                        .withIssuedAt(issuedAt)
                        .withExpiresAt(expiresAt)
                        .withClaim("userId", userId.toString())
                        .sign(jwtAlgo);

                var accessToken = accessTokenFactory.create(jwt, userId);
                var accessTokenDTO = accessTokenMapper.toDTO(accessToken);

                var result = new CreateAccessTokenResults.Success(jwt);

                events.publishEvent(new AccessTokenCreatedEvent(accessTokenDTO));
                return result;
            }

        } catch (Exception e) {
            return new CreateAccessTokenResults.SystemError(e.getMessage());
        }

        //This should never run. But for application correctness, we return an error.
        return new CreateAccessTokenResults.SystemError("Unexpected error");
    }
}
