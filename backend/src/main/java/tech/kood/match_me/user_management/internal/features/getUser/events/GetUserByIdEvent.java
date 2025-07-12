
package tech.kood.match_me.user_management.internal.features.getUser.events;

import java.util.Optional;

import tech.kood.match_me.user_management.models.User;


/**
 * Event representing the retrieval of a user by their id.
 * @param user      The {@link User} retrieved by email
 * @param tracingId An optional external tracing identifier for request correlation
 */
public record GetUserByIdEvent(User user, Optional<String> tracingId) {
    public GetUserByIdEvent {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (tracingId == null) {
            throw new IllegalArgumentException("Tracing ID cannot be null");
        }
    }
}