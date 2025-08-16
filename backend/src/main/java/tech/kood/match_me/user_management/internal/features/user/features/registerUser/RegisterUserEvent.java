package tech.kood.match_me.user_management.internal.features.user.features.registerUser;


/**
 * Event representing the registration of a new user.
 *
 * @param User      The registered user;
 * @throws IllegalArgumentException if {@code user} is {@code null}
 */
public record RegisterUserEvent(
    RegisterUserRequest request,
    RegisterUserResults result
) {
}