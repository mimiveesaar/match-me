package tech.kood.match_me.user_management.internal.domain.features.login;

public record LoginEvent(
        LoginRequest request,
        LoginResults result) {
}
