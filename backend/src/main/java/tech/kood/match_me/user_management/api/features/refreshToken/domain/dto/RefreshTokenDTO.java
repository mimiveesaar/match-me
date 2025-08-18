package tech.kood.match_me.user_management.api.features.refreshToken.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.user_management.features.user.domain.api.UserIdDTO;

import java.time.Instant;

public record RefreshTokenDTO(
        @NotNull @Valid RefreshTokenIdDTO id,
        @NotNull @Valid UserIdDTO userId,
        @NotEmpty String secret,
        @NotNull Instant createdAt,
        @NotNull Instant expiresAt
) {
}
