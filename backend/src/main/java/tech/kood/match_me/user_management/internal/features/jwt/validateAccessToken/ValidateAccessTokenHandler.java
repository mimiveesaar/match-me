package tech.kood.match_me.user_management.internal.features.jwt.validateAccessToken;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import tech.kood.match_me.user_management.internal.common.cqrs.QueryHandler;
import tech.kood.match_me.user_management.internal.domain.models.AccessToken;
import tech.kood.match_me.user_management.features.user.domain.internal.model.user.UserId;

@Service
public class ValidateAccessTokenHandler
        implements QueryHandler<ValidateAccessTokenRequest, ValidateAccessTokenResults> {

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

            var decodedJWT = jwtVerifier.verify(request.getJwtToken());
            Claim userId = decodedJWT.getClaim("userId");

            if (userId.isMissing()) {
                throw new IllegalArgumentException("Invalid JWT: userId claim is missing");
            }

            var accessToken =
                    AccessToken.of(request.getJwtToken(), UserId.fromString(userId.asString()));

            var result = ValidateAccessTokenResults.Success.of(accessToken, request.getRequestId(),
                    request.getTracingId());
            events.publishEvent(new ValidateAccessTokenEvent(request, result));

            return result;
        } catch (JWTVerificationException | IllegalArgumentException e) {
            var result = ValidateAccessTokenResults.InvalidToken.of(request.getJwtToken(),
                    request.getRequestId(), request.getTracingId());
            events.publishEvent(new ValidateAccessTokenEvent(request, result));
            return result;
        }
    }
}
