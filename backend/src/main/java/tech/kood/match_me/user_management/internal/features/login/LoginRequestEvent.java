package tech.kood.match_me.user_management.internal.features.login;

public record LoginRequestEvent(
        LoginRequest request,
        LoginRequestResults result) {
}
