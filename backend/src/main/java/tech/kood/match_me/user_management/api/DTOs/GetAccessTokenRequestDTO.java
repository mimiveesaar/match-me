package tech.kood.match_me.user_management.api.DTOs;

import jakarta.validation.constraints.NotEmpty;

public record GetAccessTokenRequestDTO(@NotEmpty String refreshToken) {
}
