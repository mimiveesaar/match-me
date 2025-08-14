package tech.kood.match_me.user_management.internal.features.jwt.createAccessToken;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import tech.kood.match_me.user_management.UserManagementConfig;
import tech.kood.match_me.user_management.internal.common.cqrs.CommandHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.getToken.GetRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.features.refreshToken.getToken.GetRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.features.refreshToken.getToken.GetRefreshTokenResults;

@Service
public class CreateAccessTokenHandler
                implements CommandHandler<CreateAccessTokenRequest, CreateAccessTokenResults> {

        private final ApplicationEventPublisher events;

        private final GetRefreshTokenHandler getRefreshTokenHandler;

        private final UserManagementConfig userManagementConfig;

        private final Algorithm jwtAlgo;

        public CreateAccessTokenHandler(ApplicationEventPublisher events,
                        GetRefreshTokenHandler getRefreshTokenHandler,
                        UserManagementConfig userManagementConfig,
                        @Qualifier("userManagementJwtAlgorithm") Algorithm jwtAlgo) {
                this.events = events;
                this.getRefreshTokenHandler = getRefreshTokenHandler;
                this.userManagementConfig = userManagementConfig;
                this.jwtAlgo = jwtAlgo;
        }

        public CreateAccessTokenResults handle(CreateAccessTokenRequest request) {
                try {
                        var refreshTokenResult = getRefreshTokenHandler
                                        .handle(GetRefreshTokenRequest.of(request.getRequestId(),
                                                        request.getRefreshToken(),
                                                        request.getTracingId()));

                        if (refreshTokenResult instanceof GetRefreshTokenResults.InvalidToken invalidToken) {
                                var result = CreateAccessTokenResults.InvalidToken.of(
                                                invalidToken.getRefreshToken(),
                                                request.getRequestId(), request.getTracingId());
                                events.publishEvent(new CreateAccessTokenEvent(request, result));
                                return result;
                        }

                        if (refreshTokenResult instanceof GetRefreshTokenResults.SystemError systemError) {
                                var result = CreateAccessTokenResults.SystemError.of(
                                                systemError.getMessage(), request.getRequestId(),
                                                request.getTracingId());
                                events.publishEvent(new CreateAccessTokenEvent(request, result));
                                return result;
                        }

                        if (refreshTokenResult instanceof GetRefreshTokenResults.Success refreshToken) {

                                // Generate JWT token using the refresh token's user ID.
                                String jwt = JWT.create()
                                                .withIssuer(userManagementConfig.getJwtIssuer())
                                                .withIssuedAt(Instant.now())
                                                .withExpiresAt(Instant.now()
                                                                .plusSeconds(userManagementConfig
                                                                                .getJwtExpiration()))
                                                .withClaim("userId",
                                                                refreshToken.getRefreshToken()
                                                                                .toString())
                                                .sign(jwtAlgo);

                                var result = CreateAccessTokenResults.Success.of(jwt,
                                                request.getRequestId(), request.getTracingId());

                                events.publishEvent(new CreateAccessTokenEvent(request, result));
                                return result;
                        }

                        var result = CreateAccessTokenResults.SystemError.of(
                                        "Unexpected result from refresh token handler",
                                        request.getRequestId(), request.getTracingId());
                        events.publishEvent(new CreateAccessTokenEvent(request, result));
                        return result;

                } catch (Exception e) {
                        var result = CreateAccessTokenResults.SystemError.of(
                                        "An unexpected error occurred: " + e.getMessage(),
                                        request.getRequestId(), request.getTracingId());
                        events.publishEvent(new CreateAccessTokenEvent(request, result));
                        return result;
                }
        }
}
