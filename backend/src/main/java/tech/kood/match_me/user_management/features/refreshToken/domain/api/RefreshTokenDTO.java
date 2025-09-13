package tech.kood.match_me.user_management.features.refreshToken.domain.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.domain.api.UserIdDTO;

import java.time.Instant;

public record RefreshTokenDTO(
        @NotNull @Valid @JsonProperty("id") RefreshTokenIdDTO id,
        @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId,
        @NotNull @Valid @JsonProperty("secret") RefreshTokenSecretDTO secret,
        @NotNull Instant createdAt,
        @NotNull Instant expiresAt
) {
}
