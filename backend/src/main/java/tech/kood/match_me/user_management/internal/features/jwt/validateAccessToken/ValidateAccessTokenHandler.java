package tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.Claim;

import tech.kood.match_me.user_management.models.AccessToken;

@Service
public class ValidateAccessTokenHandler {

    private final JWTVerifier jwtVerifier;

    private final ApplicationEventPublisher events;

    public ValidateAccessTokenHandler(@Qualifier("userManagementJwtVerifier") JWTVerifier jwtVerifier,
            ApplicationEventPublisher events) {
        this.jwtVerifier = jwtVerifier;
        this.events = events;
    }

    public ValidateAccessTokenResults handle(ValidateAccessTokenRequest request) {
        if (request.jwtToken() == null || request.jwtToken().isEmpty()) {
            var result = new ValidateAccessTokenResults.InvalidRequest("Token cannot be null or empty",
                    request.tracingId());
            events.publishEvent(new ValidateAccessTokenEvent(request, result));
            return result;
        }

        try {
            var decodedJWT = jwtVerifier.verify(request.jwtToken());
            Claim userId = decodedJWT.getClaim("userId");

            if (userId.isMissing() || userId.toString().isBlank()) {
                var result = new ValidateAccessTokenResults.InvalidToken(request.jwtToken(), request.tracingId());
                events.publishEvent(new ValidateAccessTokenEvent(request, result));
                return result;
            }

            var accessToken = new AccessToken(request.jwtToken(), userId.asString());
            var result = new ValidateAccessTokenResults.Success(accessToken, request.tracingId());
            events.publishEvent(new ValidateAccessTokenEvent(request, result));

            return result;
        } catch (Exception e) {
            var result = new ValidateAccessTokenResults.InvalidToken(request.jwtToken(), request.tracingId());
            events.publishEvent(new ValidateAccessTokenEvent(request, result));
            return result;
        }
    }
}
