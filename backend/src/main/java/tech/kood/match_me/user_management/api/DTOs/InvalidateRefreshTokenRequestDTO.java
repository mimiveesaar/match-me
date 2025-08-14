package tech.kood.match_me.user_management.api.DTOs;

import jakarta.validation.constraints.NotEmpty;

public record InvalidateRefreshTokenRequestDTO(@NotEmpty String refreshToken) {
}
