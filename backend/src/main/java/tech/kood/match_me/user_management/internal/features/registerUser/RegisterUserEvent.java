package tech.kood.match_me.user_management.internal.features.registerUser;


/**
 * Event representing the registration of a new user.
 *
 * @param user      The registered user; 
 * @throws IllegalArgumentException if {@code user} is {@code null}
 */
public record RegisterUserEvent(
    RegisterUserRequest request,
    RegisterUserResults result
) {
}