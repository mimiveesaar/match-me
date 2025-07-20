package tech.kood.match_me.user_management.internal.features.getUser.requests;

import java.util.Optional;
import java.util.UUID;

import lombok.Builder;

public record GetUserByUsernameRequest(
    UUID requestId,
    String username,
    Optional<String> tracingId
    ) {
    public GetUserByUsernameRequest {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (tracingId == null) {
            throw new IllegalArgumentException("Tracing ID cannot be null");
        }
        if (requestId == null) {
            throw new IllegalArgumentException("Request ID cannot be null");
        }
    }
    GetUserByUsernameRequest withUsername(String newUsername) {
        return new GetUserByUsernameRequest(requestId, newUsername, tracingId);
    }
    GetUserByUsernameRequest withTracingId(String newTracingId) {
        return new GetUserByUsernameRequest(requestId, username, Optional.ofNullable(newTracingId));
    }
}