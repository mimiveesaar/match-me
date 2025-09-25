package tech.kood.match_me.user_management.features.refreshToken.actions.getToken.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenSecretDTO;


public record GetRefreshTokenRequest(@NotNull @Valid @JsonProperty("shared_secret") RefreshTokenSecretDTO secret) {

    public GetRefreshTokenRequest withToken(RefreshTokenSecretDTO secret) {
        return new GetRefreshTokenRequest(secret);
    }
}