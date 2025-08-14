package tech.kood.match_me.user_management.api.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDTO(@Email String email, @NotEmpty String password) {
}
