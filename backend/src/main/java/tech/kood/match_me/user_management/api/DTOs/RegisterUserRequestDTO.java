package tech.kood.match_me.user_management.api.DTOs;

import jakarta.validation.constraints.NotEmpty;

public record RegisterUserRequestDTO(@NotEmpty String username, @NotEmpty String email,
                @NotEmpty String password) {
}
