package tech.kood.match_me.user_management.internal.features.registerUser;

import java.util.Optional;

import tech.kood.match_me.user_management.models.User;


/**
 * Event representing the registration of a new user.
 *
 * @param user      The registered user; 
 * @param tracingId An optional tracing identifier for distributed tracing.
 * @throws IllegalArgumentException if {@code user} is {@code null}
 */
public record UserRegisteredEvent(
    User user,
    Optional<String> tracingId
) {
    public UserRegisteredEvent {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
    }
}