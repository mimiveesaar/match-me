package tech.kood.match_me.user_management.features.accessToken.features.createAccessToken.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenSecretDTO;

public record CreateAccessTokenRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                                       @NotEmpty @Valid @JsonProperty("secret") RefreshTokenSecretDTO secret,
                                       @Nullable @JsonProperty("tracing_id") String tracingId) {

}