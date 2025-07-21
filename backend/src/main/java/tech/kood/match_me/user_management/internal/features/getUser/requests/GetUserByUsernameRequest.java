package tech.kood.match_me.user_management.internal.features.getUser.requests;

import java.util.Optional;
import java.util.UUID;

public record GetUserByUsernameRequest(
    UUID requestId,
    String username,
    Optional<String> tracingId
) {
        GetUserByUsernameRequest withUsername(String newUsername) {
            return new GetUserByUsernameRequest(requestId, newUsername, tracingId);
        }
        GetUserByUsernameRequest withTracingId(String newTracingId) {
            return new GetUserByUsernameRequest(requestId, username, Optional.ofNullable(newTracingId));
        }
}