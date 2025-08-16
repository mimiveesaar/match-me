package tech.kood.match_me.user_management.api.features.user.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UserDTO(
        @Valid @NotNull UserIdDTO id,
        @Valid @NotNull EmailDTO email,
        @Valid @NotNull HashedPasswordDTO hashedPassword,
        @NotNull Instant createdAt,
        @NotNull Instant updatedAt
) {
}
