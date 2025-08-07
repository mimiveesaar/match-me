package tech.kood.match_me.user_management.internal.domain.features.jwt.getAccessToken;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import tech.kood.match_me.user_management.UserManagementConfig;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.getToken.GetRefreshTokenHandler;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.getToken.GetRefreshTokenRequest;
import tech.kood.match_me.user_management.internal.domain.features.refreshToken.getToken.GetRefreshTokenResults;

@Service
public class GetAccessTokenHandler {

    private final ApplicationEventPublisher events;

    private final GetRefreshTokenHandler getRefreshTokenHandler;

    private final UserManagementConfig userManagementConfig;

    private final Algorithm jwtAlgo;

    public GetAccessTokenHandler(ApplicationEventPublisher events,
            GetRefreshTokenHandler getRefreshTokenHandler,
            UserManagementConfig userManagementConfig,
            @Qualifier("userManagementJwtAlgorithm") Algorithm jwtAlgo) {
        this.events = events;
        this.getRefreshTokenHandler = getRefreshTokenHandler;
        this.userManagementConfig = userManagementConfig;
        this.jwtAlgo = jwtAlgo;
    }

    public GetAccessTokenResults handle(GetAccessTokenRequest request) {
        try {
            if (request.refreshToken() == null || request.refreshToken().isBlank()) {
                var result = new GetAccessTokenResults.InvalidRequest(
                        "Refresh token must not be null or empty", request.tracingId());
                events.publishEvent(new GetAccessTokenEvent(request, result));
                return result;
            }

            var refreshTokenResult = getRefreshTokenHandler.handle(
                    new GetRefreshTokenRequest(request.refreshToken(), request.tracingId()));

            if (refreshTokenResult instanceof GetRefreshTokenResults.InvalidToken invalidToken) {
                var result = new GetAccessTokenResults.InvalidToken(invalidToken.token(),
                        request.tracingId());
                events.publishEvent(new GetAccessTokenEvent(request, result));
                return result;
            }

            if (refreshTokenResult instanceof GetRefreshTokenResults.InvalidRequest invalidRequest) {
                var result = new GetAccessTokenResults.InvalidRequest(invalidRequest.message(),
                        request.tracingId());
                events.publishEvent(new GetAccessTokenEvent(request, result));
                return result;
            }

            if (refreshTokenResult instanceof GetRefreshTokenResults.SystemError systemError) {
                var result = new GetAccessTokenResults.SystemError(systemError.message(),
                        request.tracingId());
                events.publishEvent(new GetAccessTokenEvent(request, result));
                return result;
            }

            if (refreshTokenResult instanceof GetRefreshTokenResults.Success refreshToken) {

                // Generate JWT token using the refresh token's user ID.
                String token = JWT.create().withIssuer(userManagementConfig.getJwtIssuer())
                        .withIssuedAt(Instant.now())
                        .withExpiresAt(
                                Instant.now().plusSeconds(userManagementConfig.getJwtExpiration()))
                        .withClaim("userId", refreshToken.token().userId().toString())
                        .sign(jwtAlgo);

                var result = new GetAccessTokenResults.Success(token, request.tracingId());

                events.publishEvent(new GetAccessTokenEvent(request, result));
                return result;
            }

            var result = new GetAccessTokenResults.SystemError(
                    "Unexpected result from refresh token handler", request.tracingId());
            events.publishEvent(new GetAccessTokenEvent(request, result));
            return result;

        } catch (Exception e) {
            var result = new GetAccessTokenResults.SystemError(
                    "An unexpected error occurred: " + e.getMessage(), request.tracingId());
            events.publishEvent(new GetAccessTokenEvent(request, result));
            return result;
        }
    }
}
