package tech.kood.match_me.user_management.internal.features.user.features.registerUser;

public record RegisterUserEvent(
    RegisterUserRequest request,
    RegisterUserResults result
) {
}