package tech.kood.match_me.user_management.internal.domain.features.jwt.validateAccessToken;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.Claim;

import tech.kood.match_me.user_management.internal.domain.models.AccessToken;

@Service
public class ValidateAccessTokenHandler {

    private final JWTVerifier jwtVerifier;

    private final ApplicationEventPublisher events;

    public ValidateAccessTokenHandler(
            @Qualifier("userManagementJwtVerifier") JWTVerifier jwtVerifier,
            ApplicationEventPublisher events) {
        this.jwtVerifier = jwtVerifier;
        this.events = events;
    }

    public ValidateAccessTokenResults handle(ValidateAccessTokenRequest request) {
        try {

            var decodedJWT = jwtVerifier.verify(request.jwtToken);
            Claim userId = decodedJWT.getClaim("userId");


            var accessToken = new AccessToken(request.jwtToken, userId.asString());
            var result = ValidateAccessTokenResults.Success.of(accessToken, request.requestId,
                    request.tracingId);
            events.publishEvent(new ValidateAccessTokenEvent(request, result));

            return result;
        } catch (Exception e) {
            var result = ValidateAccessTokenResults.InvalidToken.of(request.jwtToken,
                    request.requestId, request.tracingId);
            events.publishEvent(new ValidateAccessTokenEvent(request, result));
            return result;
        }
    }
}
