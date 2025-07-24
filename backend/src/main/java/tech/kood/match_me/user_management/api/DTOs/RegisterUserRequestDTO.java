package tech.kood.match_me.user_management.api.DTOs;

public record RegisterUserRequestDTO(
        String username,
        String email,
        String password) {
}
