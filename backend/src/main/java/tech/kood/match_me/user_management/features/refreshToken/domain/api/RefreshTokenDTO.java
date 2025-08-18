package tech.kood.match_me.user_management.features.refreshToken.domain.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;

import java.time.Instant;

public record RefreshTokenDTO(
        @NotNull @Valid @JsonProperty("id") RefreshTokenIdDTO id,
        @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId,
        @NotEmpty @Valid @JsonProperty("shared_secret") SharedSecretDTO sharedSecret,
        @NotNull Instant createdAt,
        @NotNull Instant expiresAt
) {
}
