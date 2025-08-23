package tech.kood.match_me.user_management.features.user.actions.login.api;

public interface LoginCommandHandler {
    LoginResults handle(LoginRequest request);
}
