package tech.kood.match_me.user_management.features.accessToken.features.validateAccessToken.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record ValidateAccessTokenRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                         @NotBlank @JsonProperty("jwt") String jwtToken,
                                         @Nullable @JsonProperty("tracing_id") String tracingId) {


    public ValidateAccessTokenRequest(String jwtToken, @Nullable String tracingId) {
        this(UUID.randomUUID(), jwtToken, tracingId);
    }

    public ValidateAccessTokenRequest withJwtToken(ValidateAccessTokenRequest request,
                                                   String jwtToken) {
        return new ValidateAccessTokenRequest(request.requestId(), jwtToken,
                request.tracingId());
    }

    public ValidateAccessTokenRequest withTracingId(ValidateAccessTokenRequest request,
                                                    String tracingId) {
        return new ValidateAccessTokenRequest(request.requestId(), request.jwtToken(),
                tracingId);
    }
}